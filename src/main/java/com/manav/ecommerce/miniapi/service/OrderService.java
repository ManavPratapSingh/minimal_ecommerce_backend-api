package com.manav.ecommerce.miniapi.service;

import com.manav.ecommerce.miniapi.model.*;
import com.manav.ecommerce.miniapi.repository.OrderRepository;
import com.manav.ecommerce.miniapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrderFromCart(String userId, String sessionId) {
        List<CartItem> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot create order from empty cart");
        }

        Order order = new Order();
        order.setUserId(userId); // Or sessionId if no user auth
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(Instant.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));

            // Check stock?
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Update stock (Simple decrement for now, typically locked)
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // Use current price or cart price? typically current DB price or
                                                    // verified price.

            // Calculate item total
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order creation
        cartService.clearCart(sessionId);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
