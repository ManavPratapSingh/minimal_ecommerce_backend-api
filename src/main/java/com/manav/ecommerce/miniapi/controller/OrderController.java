package com.manav.ecommerce.controller;

import com.manav.ecommerce.model.Order;
import com.manav.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam String userId, @RequestParam String sessionId) {
        // In real app, userId comes from auth principal.
        // Here we take it as param to link "guest session" or "user".
        Order order = orderService.createOrderFromCart(userId, sessionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
