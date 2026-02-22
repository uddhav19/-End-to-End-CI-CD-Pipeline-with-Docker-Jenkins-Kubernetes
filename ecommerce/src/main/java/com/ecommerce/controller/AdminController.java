package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping
    public String adminDashboard(HttpSession session, Model model) {
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        model.addAttribute("recentOrders", orderService.getAllOrders().stream().limit(5).toList());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts(HttpSession session, Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(HttpSession session, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, HttpSession session, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("cartCount", cartService.getCartItemCount(session));
                    return "admin/product-form";
                })
                .orElse("redirect:/admin/products");
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
                              BindingResult result,
                              HttpSession session,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cartCount", cartService.getCartItemCount(session));
            return "admin/product-form";
        }
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product saved successfully!");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Product deactivated.");
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String manageOrders(HttpSession session, Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam Order.OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Order status updated.");
        return "redirect:/admin/orders";
    }
}
