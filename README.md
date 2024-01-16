# Car Sharing App
_This API empowers users with seamless access to vital data and functionalities, enhancing their interactions with the car-sharing service._

## Table of Contents

- [️Introduction](#introduction)
- [️Key Features](#key-features)
- [Technologies](#technologies)
- [Endpoint Overview](#endpoint-overview)
- [Potential Improvements](#potential-improvements)
- [Summary](#summary)

## Introduction

Welcome to the Car Sharing App, a straightforward and efficient platform designed to simplify the management of vehicle rentals. Our application is crafted to provide users with essential features for seamless car rental experiences. With a focus on simplicity and functionality, the Car Sharing App caters to both customers and managers, offering role-based access for efficient control and oversight.

##  ️Key Features

1. **Comprehensive Car Access:** Explore a wide array of cars with both customer and manager authorities, providing a comprehensive overview of available vehicles. Enjoy a seamless experience accessing details on all cars, fostering transparency and accessibility.

2. **Personalized Car Searches:** Effortlessly find specific car details by utilizing a tailored search feature. Retrieve information about a particular car with ease, enhancing the efficiency of the car-sharing process.

3. **Efficient Car Inventory Management:** Empower managers to expand the car inventory effortlessly. Add new cars to the system with a user-friendly mechanism, ensuring an up-to-date and diverse fleet.

4. **Dynamic Information Updates:** Keep car information current and accurate, allowing managers to make dynamic updates. Modify various details about a specific car, including specifications, availability, or any other pertinent information.

## Technologies

Our Car Sharing App leverages a modern technology stack to deliver a reliable and efficient platform. Here are the key technologies and frameworks incorporated into the development process:

***Core Framework and Dependencies***

**Spring Boot:** The core framework for building Java-based applications, providing essential features and a robust foundation.

**Spring Data JPA:** Streamlines interaction with databases through the Java Persistence API (JPA), optimizing the management of data operations.

**Spring Security:** Implements security features, including role-based access control and authentication mechanisms.


***Database Management***

**H2 Database:** Utilized for development and testing, this in-memory database provides a nimble and rapid solution, enhancing efficiency during these stages.

**MySQL:** A robust relational database management system used for production, providing scalable and reliable data storage.


***API Documentation***

**Springdoc OpenAPI:** Generates and serves API documentation using the OpenAPI specification, enhancing developer understanding and interaction.


***Validation***

**Spring Boot Starter Validation:** Includes Java Bean Validation with Hibernate Validator for data validation, ensuring data integrity.


***Testing***

**JUnit:** A commonly employed testing framework that facilitates the creation and execution of unit tests, ensuring the dependability of the code.

**Mockito:** A testing framework enabling the generation of test double objects, ensuring comprehensive unit testing capabilities.

**Spring Boot Starter Test:** Offers essential testing support for Spring Boot applications, enhancing the overall testing infrastructure.


***Other Libraries and Tools***

**Lombok:** Reduces boilerplate code by providing annotations to generate common methods, enhancing code readability.

**MapStruct:** Facilitates seamless data handling by streamlining the mapping process between Data Transfer Objects (DTOs) and entities.

**Docker:** Simplifies deployment with containerization, enabling consistent and efficient deployment across different environments.

## Endpoint Overview

In our Car Sharing App project, we've adhered to REST principles for designing our controllers.

**AuthenticationController:** handles requests for registering and logging in users (with email and password or JWT token).
+ `POST: /api/auth/registration` - Register a user.
+ `POST: /api/auth/login` - Log in with user credentials.

**CarController:** processes requests for managing cars, covering addition, updates, retrieval, and search functionalities.
+ `GET: /api/cars` - Retrieve all cars with CUSTOMER and MANAGER authorities.
+ `GET: /api/cars/{id}` - Search for a specific car with CUSTOMER and MANAGER authorities.
+ `POST: /api/cars` - Add a new car with MANAGER authority.
+ `PUT: /api/cars/{id}` - Update information about a car with MANAGER authority.
+ `DELETE: /api/cars/{id}` - Delete a car with MANAGER authority.

**PaymentController:** processes requests related to payments, covering creation, retrieval, and handling of payment sessions.
+ `GET: /api/payments` - Retrieve all payments, accessible to users with CUSTOMER and MANAGER authorities (Managers can see all payments, customers only theirs).
+ `POST: /api/payments` - Create a payment session for car rental, restricted to users with CUSTOMER authority.
+ `GET: /api/payments/success` - Success payment endpoint, automatically redirected after a successful payment, accessible to users with CUSTOMER authority.
+ `GET: /api/payments/cancel` - Canceled payment endpoint, automatically redirected after a canceled payment, accessible to users with CUSTOMER authority.

**RentalController:** processes requests related to car rentals, including functionalities for rental creation, retrieval, and updating rental status.
+ `POST: /api/rentals` - Add a new rental with CUSTOMER authority.
+ `GET: /api/rentals` - Get list of rentals with MANAGER authority.
+ `GET: /api/rentals/{id}` - Get a specific rental with CUSTOMER authority.
+ `POST: /api/rentals/{id}/return` - Set the actual return date for a rental with MANAGER authority.

**RoleController:** processes requests for handling user roles, providing functionalities for retrieving role information.
+ `GET: /api/roles` - Get all roles with ADMIN authority.

**UserController:** processes requests for handling users, offering functionalities for updating roles and user profiles.
+ `PUT: /api/users/{id}/role` - Update the role of a user by ID with ADMIN authority.
+ `PATCH: /api/users/me` - Update profile with ADMIN, MANAGER, or CUSTOMER authority.
+ `PUT: /api/users/update-password` - Update your password with ADMIN, MANAGER, or CUSTOMER authority.
+ `PUT: /api/users/update-email` - Update email for your account with ADMIN, MANAGER, or CUSTOMER authority.
+ `GET: /api/users/me` - Get profile with ADMIN, MANAGER, or CUSTOMER authority.

## Potential Improvements

While the app provides basic functionality, there are areas where enhancements could be considered:

**Improving Search Functionality:** Enhancing the search feature to allow users to find cars based on specific criteria such as model, brand, or features.

**Adding Feedback System:** Implementing a simple feedback system to collect user reviews and suggestions for service improvement.

## Summary

Our Car Sharing App aims to provide a straightforward and efficient solution for users looking to engage with vehicle rental services. Key features include:

**Car Management:** Easily access information about available cars, search for specific vehicles, and initiate rental requests.

**Secure Rental Process:** Role-based access control allows customers to create and manage rentals, while managers oversee and update rental statuses.

**Payment Integration:** Streamlined payment processes ensure a smooth transaction experience for customers, with managers having access to payment details.

**User Profile Management:** Customers, managers, and administrators can update their profiles, change passwords, and manage email preferences.

**Role-Based Access:** Administrators can efficiently update user roles, ensuring a well-defined hierarchy of access within the system.
