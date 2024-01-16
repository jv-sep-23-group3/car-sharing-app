# 🚘Car Sharing App
_The car-sharing service's interactions with users are enhanced by the seamless access to vital data and functionalities provided by this API._
## 🗂️Table of Contents

- [️Introduction](#introduction)
- [️Key Features](#key-features)
- [Technologies](#technologies)
- [Endpoint Overview](#endpoint-overview)
- [Challenges](#challenges)
- [Potential Improvements](#potential-improvements)
- [Summary](#summary)

## ✍️Introduction
The Car Sharing App is a platform that makes managing vehicle rentals simple and efficient. Our application is designed to give users the essential features they need to have a seamless car rental experience. The Car Sharing App is designed to provide simplicity and functionality to both customers and managers, with role-based access for efficient control and oversight.

## 🔑️Key Features

1. **Comprehensive Car Access:** Explore a wide array of cars with both customer and manager authorities, providing a comprehensive overview of available vehicles. Enjoy a seamless experience accessing details on all cars, fostering transparency and accessibility.

2. **Personalized Car Searches:** Effortlessly find specific car details by utilizing a tailored search feature. Retrieve information about a particular car with ease, enhancing the efficiency of the car-sharing process.

3. **Efficient Car Inventory Management:** Empower managers to expand the car inventory effortlessly. Add new cars to the system with a user-friendly mechanism, ensuring an up-to-date and diverse fleet.

4. **Dynamic Information Updates:** Keep car information current and accurate, allowing managers to make dynamic updates. Modify various details about a specific car, including specifications, availability, or any other pertinent information.

## 🛰️Technologies

_Our Car Sharing App leverages a modern technology stack to deliver a reliable and efficient platform. Here are the key technologies and frameworks incorporated into the development process:_

---
***💎 Core Framework and Dependencies 💎***

**🌟Spring Boot:** The core framework for building Java-based applications, providing essential features and a robust foundation.

**🌟Spring Data JPA:** Streamlines interaction with databases through the Java Persistence API (JPA), optimizing the management of data operations.

**🌟Spring Security:** Implements security features, including role-based access control and authentication mechanisms.

---

***💎 Database Management 💎***

**🌟MySQL:** A robust relational database management system used for production, providing scalable and reliable data storage.

---

***💎 API Documentation 💎***

**🌟Springdoc OpenAPI:** Generates and serves API documentation using the OpenAPI specification, enhancing developer understanding and interaction.

---

***💎 Validation 💎***

**🌟Spring Boot Starter Validation:** Includes Java Bean Validation with Hibernate Validator for data validation, ensuring data integrity.

---

***💎 Testing 💎***

**🌟JUnit:** A commonly employed testing framework that facilitates the creation and execution of unit tests, ensuring the dependability of the code.

**🌟Mockito:** A testing framework enabling the generation of test double objects, ensuring comprehensive unit testing capabilities.

**🌟Spring Boot Starter Test:** Offers essential testing support for Spring Boot applications, enhancing the overall testing infrastructure.

---

***💎 Other Libraries and Tools 💎***

**🌟Lombok:** Reduces boilerplate code by providing annotations to generate common methods, enhancing code readability.

**🌟MapStruct:** Facilitates seamless data handling by streamlining the mapping process between Data Transfer Objects (DTOs) and entities.

**🌟Docker:** Simplifies deployment with containerization, enabling consistent and efficient deployment across different environments.

---

## 🚨Endpoint Overview

_In our Car Sharing App project, we've adhered to REST principles for designing our controllers._

---

**AuthenticationController:** handles requests for registering and logging in users (with email and password or JWT token).
+ `POST: /api/auth/registration` - Register a user.
+ `POST: /api/auth/login` - Log in with user credentials.

---

**CarController:** processes requests for managing cars, covering addition, updates, retrieval, and search functionalities.
+ `GET: /api/cars` - Retrieve all cars with CUSTOMER and MANAGER authorities.
+ `GET: /api/cars/{id}` - Search for a specific car with CUSTOMER and MANAGER authorities.
+ `POST: /api/cars` - Add a new car with MANAGER authority.
+ `PUT: /api/cars/{id}` - Update information about a car with MANAGER authority.
+ `DELETE: /api/cars/{id}` - Delete a car with MANAGER authority.

---

**PaymentController:** processes requests related to payments, covering creation, retrieval, and handling of payment sessions.
+ `GET: /api/payments` - Retrieve all payments, accessible to users with CUSTOMER and MANAGER authorities (Managers can see all payments, customers only theirs).
+ `POST: /api/payments` - Create a payment session for car rental, restricted to users with CUSTOMER authority.
+ `GET: /api/payments/success` - Success payment endpoint, automatically redirected after a successful payment, accessible to users with CUSTOMER authority.
+ `GET: /api/payments/cancel` - Canceled payment endpoint, automatically redirected after a canceled payment, accessible to users with CUSTOMER authority.

---

**RentalController:** processes requests related to car rentals, including functionalities for rental creation, retrieval, and updating rental status.
+ `POST: /api/rentals` - Add a new rental with CUSTOMER authority.
+ `GET: /api/rentals` - Get list of rentals with MANAGER authority.
+ `GET: /api/rentals/{id}` - Get a specific rental with CUSTOMER authority.
+ `POST: /api/rentals/{id}/return` - Set the actual return date for a rental with MANAGER authority.

---

**RoleController:** processes requests for handling user roles, providing functionalities for retrieving role information.
+ `GET: /api/roles` - Get all roles with ADMIN authority.

---

**UserController:** processes requests for handling users, offering functionalities for updating roles and user profiles.
+ `PUT: /api/users/{id}/role` - Update the role of a user by ID with ADMIN authority.
+ `PATCH: /api/users/me` - Update profile with ADMIN, MANAGER, or CUSTOMER authority.
+ `PUT: /api/users/update-password` - Update your password with ADMIN, MANAGER, or CUSTOMER authority.
+ `PUT: /api/users/update-email` - Update email for your account with ADMIN, MANAGER, or CUSTOMER authority.
+ `GET: /api/users/me` - Get profile with ADMIN, MANAGER, or CUSTOMER authority.

---

## ☕Prerequisites

List the prerequisites required to run your application. Include things like:

- Java 17 [Download Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Docker [Download Docker](https://www.docker.com/)

## ⬇️Installation

1. Clone repository:
    - Open IDEA → File → New Project from Version Control and insert link: [https://github.com/jv-sep-23-group3/car-sharing-app](https://github.com/jv-sep-23-group3/car-sharing-app).
    - Or clone from the console with the command: `git clone https://github.com/jv-sep-23-group3/car-sharing-app`
2. Build project and download dependencies for Maven with command: `mvn clean install`
3. Docker Compose your project: `docker compose build` and `docker compose up`
4. If you want to test the API, you can [download](postman/CarSharingAPI.postman_collection.json) our Postman collection. You can either [download the local version](https://www.postman.com/downloads/) or use the Postman [web version](https://www.postman.com/)


## 💪Challenges

- Stripe API:
- Telegram API:
- 

## 🧠Potential Improvements


## 💬Summary


<img src="images/logo.png" style="border-radius: 20px; width: 600px">
