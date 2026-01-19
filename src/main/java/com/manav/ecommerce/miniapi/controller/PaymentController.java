package com.manav.ecommerce.miniapi.controller;

import com.manav.ecommerce.miniapi.model.Order;
import com.manav.ecommerce.miniapi.model.OrderStatus;
import com.manav.ecommerce.miniapi.service.MockPaymentService;
import com.manav.ecommerce.miniapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private MockPaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/{orderId}")
    public ResponseEntity<String> payForOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order.getStatus() == OrderStatus.PAID) {
            return ResponseEntity.badRequest().body("Order is already paid.");
        }

        boolean success = paymentService.process(order.getTotalAmount());

        if (success) {
            orderService.updateOrderStatus(orderId, OrderStatus.PAID);
            return ResponseEntity.ok("Payment successful. Order updated to PAID.");
        } else {
            return ResponseEntity.status(500).body("Payment failed.");
        }
    }
}
