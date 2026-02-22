package com.ecommerce.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private String imageUrl;

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
