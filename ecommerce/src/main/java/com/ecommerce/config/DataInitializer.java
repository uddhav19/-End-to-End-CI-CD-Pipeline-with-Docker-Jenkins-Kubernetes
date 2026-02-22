package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@shop.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .roles(Set.of("USER", "ADMIN"))
                    .enabled(true)
                    .build());
        }

        // Create regular user
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(User.builder()
                    .username("user")
                    .email("user@shop.com")
                    .password(passwordEncoder.encode("user123"))
                    .firstName("John")
                    .lastName("Doe")
                    .roles(Set.of("USER"))
                    .enabled(true)
                    .build());
        }

        // Create sample products
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder().name("iPhone 15 Pro").description("Latest Apple smartphone with A17 Pro chip, titanium design, and advanced camera system.").price(new BigDecimal("999.99")).stockQuantity(50).category("Electronics").imageUrl("https://via.placeholder.com/300x300?text=iPhone+15+Pro").active(true).build());
            productRepository.save(Product.builder().name("Samsung 4K TV 55\"").description("Crystal clear 4K display with HDR and smart TV features. Perfect for your living room.").price(new BigDecimal("649.99")).stockQuantity(20).category("Electronics").imageUrl("https://via.placeholder.com/300x300?text=Samsung+TV").active(true).build());
            productRepository.save(Product.builder().name("Sony WH-1000XM5 Headphones").description("Industry-leading noise cancellation with 30-hour battery life.").price(new BigDecimal("349.99")).stockQuantity(35).category("Electronics").imageUrl("https://via.placeholder.com/300x300?text=Sony+Headphones").active(true).build());
            productRepository.save(Product.builder().name("Nike Air Max 270").description("Iconic Air unit in the heel for maximum comfort and style. Available in multiple colors.").price(new BigDecimal("149.99")).stockQuantity(100).category("Footwear").imageUrl("https://via.placeholder.com/300x300?text=Nike+Air+Max").active(true).build());
            productRepository.save(Product.builder().name("Adidas Ultraboost 23").description("Responsive Boost midsole and Primeknit+ upper for ultimate running performance.").price(new BigDecimal("179.99")).stockQuantity(75).category("Footwear").imageUrl("https://via.placeholder.com/300x300?text=Adidas+Ultraboost").active(true).build());
            productRepository.save(Product.builder().name("Levi's 501 Jeans").description("The original blue jean since 1873. Straight fit with button fly.").price(new BigDecimal("59.99")).stockQuantity(200).category("Clothing").imageUrl("https://via.placeholder.com/300x300?text=Levis+501").active(true).build());
            productRepository.save(Product.builder().name("The North Face Jacket").description("Weatherproof jacket with DryVent technology. Lightweight and packable.").price(new BigDecimal("229.99")).stockQuantity(40).category("Clothing").imageUrl("https://via.placeholder.com/300x300?text=North+Face").active(true).build());
            productRepository.save(Product.builder().name("MacBook Air M3").description("15-inch display with M3 chip, 18-hour battery life, and fanless design.").price(new BigDecimal("1299.99")).stockQuantity(25).category("Electronics").imageUrl("https://via.placeholder.com/300x300?text=MacBook+Air").active(true).build());
            productRepository.save(Product.builder().name("Yoga Mat Premium").description("Non-slip 6mm thick yoga mat with alignment lines. Eco-friendly TPE material.").price(new BigDecimal("39.99")).stockQuantity(150).category("Sports").imageUrl("https://via.placeholder.com/300x300?text=Yoga+Mat").active(true).build());
            productRepository.save(Product.builder().name("Coffee Maker Pro").description("12-cup programmable coffee maker with thermal carafe and built-in grinder.").price(new BigDecimal("89.99")).stockQuantity(60).category("Kitchen").imageUrl("https://via.placeholder.com/300x300?text=Coffee+Maker").active(true).build());
            productRepository.save(Product.builder().name("Instant Pot Duo 7-in-1").description("Pressure cooker, slow cooker, rice cooker, steamer, sauté, yogurt maker, and warmer in one.").price(new BigDecimal("79.99")).stockQuantity(80).category("Kitchen").imageUrl("https://via.placeholder.com/300x300?text=Instant+Pot").active(true).build());
            productRepository.save(Product.builder().name("Dumbbells Set 20kg").description("Adjustable dumbbell set from 2.5kg to 20kg. Space-saving design for home gym.").price(new BigDecimal("129.99")).stockQuantity(30).category("Sports").imageUrl("https://via.placeholder.com/300x300?text=Dumbbells").active(true).build());
        }
    }
}
