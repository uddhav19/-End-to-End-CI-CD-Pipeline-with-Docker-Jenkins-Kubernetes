package com.ecommerce.controller;

import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        productService.getProductById(productId).ifPresentOrElse(
                product -> {
                    if (product.getStockQuantity() >= quantity) {
                        cartService.addToCart(session, product, quantity);
                        redirectAttributes.addFlashAttribute("success", "'" + product.getName() + "' added to cart!");
                    } else {
                        redirectAttributes.addFlashAttribute("error", "Insufficient stock available.");
                    }
                },
                () -> redirectAttributes.addFlashAttribute("error", "Product not found.")
        );
        return "redirect:/products/" + productId;
    }

    @PostMapping("/update/{productId}")
    public String updateCart(@PathVariable Long productId,
                             @RequestParam int quantity,
                             HttpSession session) {
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}
