package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping
    public String myOrders(Authentication auth, HttpSession session, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("orders", orderService.getUserOrders(user));
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Authentication auth,
                              HttpSession session, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        return orderService.getOrderById(id)
                .filter(order -> order.getUser().getUsername().equals(auth.getName())
                        || user.getRoles().contains("ADMIN"))
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("cartCount", cartService.getCartItemCount(session));
                    return "orders/detail";
                })
                .orElse("redirect:/orders");
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        if (cartService.getCart(session).isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "orders/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             Authentication auth, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            Order order = orderService.placeOrder(user, shippingAddress, session);
            redirectAttributes.addFlashAttribute("success", "Order #" + order.getId() + " placed successfully!");
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/checkout";
        }
    }
}
