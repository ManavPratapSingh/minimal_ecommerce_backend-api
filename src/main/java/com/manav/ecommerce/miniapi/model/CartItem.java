package com.manav.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
