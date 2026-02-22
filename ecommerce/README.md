# 🛍️ ShopZone - Spring Boot E-Commerce Application

A full-featured e-commerce web application built with **Spring Boot** (backend) and **Thymeleaf + Bootstrap 5** (frontend).

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application
```bash
cd ecommerce-app
mvn spring-boot:run
```
Then open: **http://localhost:8080**

---

## 👥 Demo Accounts

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | `admin`  | `admin123`|
| User  | `user`   | `user123` |

---

## 🏗️ Project Structure

```
src/main/java/com/ecommerce/
├── EcommerceApplication.java       # Main entry point
├── config/
│   ├── SecurityConfig.java         # Spring Security setup
│   └── DataInitializer.java        # Sample data loader
├── model/
│   ├── Product.java                # Product entity
│   ├── User.java                   # User entity
│   ├── Order.java                  # Order entity
│   ├── OrderItem.java              # Order line items
│   └── CartItem.java               # Session cart item (not persisted)
├── repository/
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   └── OrderRepository.java
├── service/
│   ├── ProductService.java         # Product CRUD + search
│   ├── CartService.java            # Session-based cart
│   ├── OrderService.java           # Order placement + management
│   └── UserService.java            # User registration + Spring Security
└── controller/
    ├── ProductController.java      # /products, /
    ├── CartController.java         # /cart/**
    ├── OrderController.java        # /orders/**
    ├── AuthController.java         # /login, /register
    └── AdminController.java        # /admin/**

src/main/resources/
├── application.properties          # App config (H2, Thymeleaf, etc.)
├── templates/
│   ├── index.html                  # Homepage
│   ├── auth/login.html             # Login page
│   ├── auth/register.html          # Registration page
│   ├── products/list.html          # Product catalog
│   ├── products/detail.html        # Product detail + add to cart
│   ├── cart/view.html              # Shopping cart
│   ├── orders/checkout.html        # Checkout page
│   ├── orders/list.html            # My orders
│   ├── orders/detail.html          # Order details
│   ├── admin/dashboard.html        # Admin dashboard
│   ├── admin/products.html         # Manage products
│   ├── admin/product-form.html     # Add/edit product
│   ├── admin/orders.html           # Manage orders
│   └── fragments/nav.html          # Shared navbar/footer
└── static/
    ├── css/style.css               # Custom CSS
    └── js/app.js                   # Custom JS
```

---

## ✨ Features

### Storefront
- 🏠 **Homepage** with hero banner, category grid, featured products
- 🔍 **Search** products by keyword
- 📂 **Filter** by category
- 🛍️ **Product detail** page with quantity selector
- 🛒 **Shopping cart** (session-based, no login required to browse)
- 💳 **Checkout** with shipping address
- 📦 **Order history** and order detail with status timeline

### Admin Panel (`/admin`)
- 📊 **Dashboard** with stats and recent orders
- ➕ **Add/Edit/Deactivate** products
- 🔄 **Update order status** (Pending → Confirmed → Shipped → Delivered)

### Security
- 🔐 Spring Security with BCrypt password encoding
- 👤 Role-based access: `USER` and `ADMIN`
- 🛡️ CSRF protection on all forms

---

## 🛠️ Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Backend    | Spring Boot 3.2, Spring MVC       |
| Security   | Spring Security 6                 |
| Database   | H2 (in-memory), Spring Data JPA   |
| Frontend   | Thymeleaf, Bootstrap 5, Vanilla JS|
| Build      | Maven                             |

---

## 🗄️ Database

Uses **H2 in-memory database** (auto-created on startup, reset on restart).

Access H2 Console: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:ecommercedb`
- Username: `sa` | Password: *(empty)*

---

## 🔌 Key Endpoints

| URL | Description |
|-----|-------------|
| `/` | Homepage |
| `/products` | Product catalog |
| `/products/{id}` | Product detail |
| `/cart` | View cart |
| `/cart/add/{id}` | Add to cart (POST) |
| `/orders/checkout` | Checkout page |
| `/orders` | My orders |
| `/login` | Login |
| `/register` | Register |
| `/admin` | Admin dashboard |
| `/admin/products` | Manage products |
| `/admin/orders` | Manage orders |
| `/h2-console` | Database console |

---

## 📝 To-Do / Enhancements
- [ ] Switch to MySQL/PostgreSQL for production
- [ ] Add real payment gateway (Stripe)
- [ ] Product image upload
- [ ] Pagination for product listings
- [ ] Email notifications on order
- [ ] Product reviews/ratings
