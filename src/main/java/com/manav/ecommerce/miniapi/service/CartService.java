package com.manav.ecommerce.miniapi.service;

import com.manav.ecommerce.miniapi.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRY_DAYS = 7;

    public void addToCart(String sessionId, CartItem item) {
        String key = CART_PREFIX + sessionId;
        // Using a List for simplicity, could be a Hash for better updates
        redisTemplate.opsForList().rightPush(key, item);
        redisTemplate.expire(key, CART_EXPIRY_DAYS, TimeUnit.DAYS);
    }

    public List<CartItem> getCartItems(String sessionId) {
        String key = CART_PREFIX + sessionId;
        List<Object> items = redisTemplate.opsForList().range(key, 0, -1);
        List<CartItem> cartItems = new ArrayList<>();
        if (items != null) {
            for (Object obj : items) {
                if (obj instanceof CartItem) {
                    cartItems.add((CartItem) obj);
                }
            }
        }
        return cartItems;
    }

    public void clearCart(String sessionId) {
        String key = CART_PREFIX + sessionId;
        redisTemplate.delete(key);
    }

    public void removeFromCart(String sessionId, Long productId) {
        // Removing from a list by value property is tricky in simple Redis List.
        // For a minimal implementation, we might read all, remove, and rewrite,
        // or effectively use a Hash structure instead.
        // Given constraints, reading all and rewriting involves logic.

        // Alternative: Use Hash where Key=SessionId, HashKey=ProductId,
        // HashValue=CartItem
        // Let's implement creating a list-based approach for this 'add/remove' request
        // or stick to List if removing isn't strictly detailed.
        // Re-reading: "allows users to add/remove items"

        // Let's swap to Hash based approach for better management? No, the prompt
        // specifically says "CartItem POJO", implies list of items.
        // Let's do read-modify-write for remove.

        String key = CART_PREFIX + sessionId;
        List<Object> items = redisTemplate.opsForList().range(key, 0, -1);
        if (items != null) {
            redisTemplate.delete(key); // Clear existing
            for (Object obj : items) {
                if (obj instanceof CartItem) {
                    CartItem currentItem = (CartItem) obj;
                    if (!currentItem.getProductId().equals(productId)) {
                        redisTemplate.opsForList().rightPush(key, currentItem);
                    }
                }
            }
            redisTemplate.expire(key, CART_EXPIRY_DAYS, TimeUnit.DAYS);
        }
    }
}
