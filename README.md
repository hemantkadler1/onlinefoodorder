# 🍽️ Online Food Ordering System

A full-stack web application for ordering food online. Built using **Spring Boot (Java)** for the backend and basic **HTML/CSS/JS** for the frontend. Supports user registration, menu browsing, cart management, order placement, payment (mock), and order tracking.

---

## 🚀 Features

- 🔐 User Registration & Login (JWT-secured)
- 📋 View Food Menu
- 🛒 Add Items to Cart
- ✅ Place Orders with Quantity
- 💳 Mock Payment Gateway
- 📦 Track Order Status
- 🔐 Role-based Access (Admin/User)
- 📈 Admin Dashboard (manage menu, view orders)
- 🔎 Search Menu Items

---

## 🛠️ Tech Stack

### Backend:
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate & JPA
- MySQL
- Maven

### Frontend:
- HTML / CSS / JavaScript
- Bootstrap

---

## 💾 Database Configuration

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fooddb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_secret_key
