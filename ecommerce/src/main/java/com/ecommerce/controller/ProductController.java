package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        List<Product> featured = productService.getAllActiveProducts();
        model.addAttribute("products", featured.size() > 8 ? featured.subList(0, 8) : featured);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "index";
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String category,
                           @RequestParam(required = false) String search,
                           HttpSession session,
                           Model model) {
        List<Product> products;
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllActiveProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(session));
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, HttpSession session, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("cartCount", cartService.getCartItemCount(session));
                    return "products/detail";
                })
                .orElse("redirect:/products");
    }
}
