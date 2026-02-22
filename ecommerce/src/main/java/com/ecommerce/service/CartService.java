package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product, int quantity) {
        List<CartItem> cart = getCart(session);
        Optional<CartItem> existing = cart.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    quantity,
                    product.getImageUrl()
            );
            cart.add(item);
        }
    }

    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        List<CartItem> cart = getCart(session);
        if (quantity <= 0) {
            removeFromCart(session, productId);
        } else {
            cart.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
        }
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartItemCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
