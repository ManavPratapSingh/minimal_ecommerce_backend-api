package com.manav.ecommerce.miniapi.controller;

import com.manav.ecommerce.miniapi.model.CartItem;
import com.manav.ecommerce.miniapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam String sessionId, @RequestBody CartItem item) {
        cartService.addToCart(sessionId, item);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String sessionId) {
        return ResponseEntity.ok(cartService.getCartItems(sessionId));
    }

    @DeleteMapping("/{sessionId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String sessionId) {
        cartService.clearCart(sessionId);
        return ResponseEntity.ok("Cart cleared");
    }
}
