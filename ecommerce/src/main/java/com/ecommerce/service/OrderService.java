package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    public Order placeOrder(User user, String shippingAddress, HttpSession session) {
        List<CartItem> cartItems = cartService.getCart(session);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .status(Order.OrderStatus.PENDING)
                .totalAmount(cartService.getCartTotal(session))
                .build();

        for (CartItem cartItem : cartItems) {
            boolean stockUpdated = productService.updateStock(cartItem.getProductId(), cartItem.getQuantity());
            if (!stockUpdated) {
                throw new RuntimeException("Insufficient stock for: " + cartItem.getProductName());
            }
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(productService.getProductById(cartItem.getProductId()).orElseThrow())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .build();
            order.getItems().add(orderItem);
        }

        Order saved = orderRepository.save(order);
        cartService.clearCart(session);
        return saved;
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
