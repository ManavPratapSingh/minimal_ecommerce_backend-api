package com.manav.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For simplicity, we might store userId as a string or link to User entity if
    // it exists.
    // The prompt mentions "associated with a specific user or session ID".
    // We'll trust the user will provide a userId or session identifier.
    // The PDF had generic User entity, but let's stick to simple String for now if
    // User auth isn't fully built.
    // Actually, PDF said "User Entity", let's assume we might need a User link, or
    // just userId string for now to be "minimal".
    // I'll leave it as userId String to be flexible with the Redis session
    // approach.
    private String userId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
