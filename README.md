# 🚘Car Sharing App
_The car-sharing service's interactions with users are enhanced by the seamless access to vital data and functionalities provided by this API._
## 🗂️Table of Contents

- [️Introduction](#introduction)
- [️Key Features](#key-features)
- [Technologies](#technologies)
- [Endpoint Overview](#endpoint-overview)
- [Preconditions](#preconditions)
- [Installation](#installation)
- [Challenges](#challenges)
- [Potential Improvements](#potential-improvements)
- [Summary](#summary)

## ✍️Introduction
_The Car Sharing App is a platform that makes managing vehicle rentals simple and efficient. Our application is designed to give users the essential features they need to have a seamless car rental experience. The Car Sharing App is designed to provide simplicity and functionality to both customers and managers, with role-based access for efficient control and oversight._

## 🔑️Key Features

1. **🚗Comprehensive Car Access:** Explore a wide array of cars with both CUSTOMER and MANAGER authorities, providing a comprehensive overview of available vehicles. Enjoy a seamless experience accessing details on all cars, fostering transparency and accessibility.


2. **🚐Personalized Car Searches:** Effortlessly find specific car details by utilizing a tailored search feature. Retrieve information about a particular car with ease, enhancing the efficiency of the car-sharing process.


3. **🚏Efficient Car Inventory Management:** Provide managers with the ability to expand the car inventory easily by adding new cars to the system through a user-friendly mechanism, guaranteeing an up-to-date and diverse fleet.


4. **🚦Dynamic Information Updates:** Maintain car information that is current and accurate, enabling managers to make dynamic updates. Change the details of a specific car, such as its specifications, availability, or other relevant information.


## 🛰️Technologies

_Our Car Sharing App leverages a modern technology stack to deliver a reliable and efficient platform. Here are the key technologies and frameworks incorporated into the development process:_

---
***💎 Core Framework and Dependencies***

**🌟Spring Boot:** The core framework for building Java-based applications, providing essential features and a robust foundation.

**🌟Spring Data JPA:** Streamlines interaction with databases through the Java Persistence API (JPA), optimizing the management of data operations.

**🌟Spring Security:** Implements security features, including role-based access control and authentication mechanisms.

---

***💎 Database Management***

**🌟MySQL:** A robust relational database management system used for production, providing scalable and reliable data storage.

---

***💎 API Documentation***

**🌟Springdoc OpenAPI:** Generates and serves API documentation using the OpenAPI specification, enhancing developer understanding and interaction.

---

***💎 Validation***

**🌟Spring Boot Starter Validation:** Includes Java Bean Validation with Hibernate Validator for data validation, ensuring data integrity.

---

***💎 Testing***

**🌟JUnit:** A commonly employed testing framework that facilitates the creation and execution of unit tests, ensuring the dependability of the code.

**🌟Mockito:** A testing framework enabling the generation of test double objects, ensuring comprehensive unit testing capabilities.

**🌟Spring Boot Starter Test:** Offers essential testing support for Spring Boot applications, enhancing the overall testing infrastructure.

---

***💎 Other Libraries and Tools***

**🌟Lombok:** Reduces boilerplate code by providing annotations to generate common methods, enhancing code readability.

**🌟MapStruct:** Facilitates seamless data handling by streamlining the mapping process between Data Transfer Objects (DTOs) and entities.

**🌟Docker:** Simplifies deployment with containerization, enabling consistent and efficient deployment across different environments.

---

***💎External API:***

**🌟Telegram API:** Keeping users informed, the Telegram API facilitates instant notifications, enhancing the overall user experience.

**🌟Stripe:** Streamlining online payments, Stripe provides a secure and user-friendly payment processing solution for our platform.

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

## ☕Preconditions

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

In the development of our Car Sharing Service project, we encountered several challenges, particularly in the integration of two critical components: the Telegram API and the Stripe API.

📩Telegram API Integration
-
**Challenge:** Implementing the Telegram API posed a unique challenge as it was the first time our team had worked with this particular messaging platform. We needed to ensure that notifications were sent seamlessly to users through Telegram.

**Solution:** To overcome this challenge, we conducted thorough research, followed the Telegram API documentation meticulously, and iteratively tested our implementation. This allowed us to successfully integrate Telegram for instant notifications, enhancing user communication and experience within our platform.

💳Stripe API Integration
-
**Challenge:** Integrating the Stripe API required careful handling of payment processing, user data, and security. This was a complex task, especially considering the sensitive nature of financial transactions.

**Solution:** To address this challenge, we followed industry best practices for payment processing, including tokenization and secure data transmission. Additionally, we regularly reviewed and updated our integration to maintain compliance with Stripe's security standards. As a result, we established a robust payment system that ensures seamless and secure online transactions for our users.

---

💡These challenges served as valuable learning experiences for our team, and the solutions implemented have not only enhanced the functionality of our Car Sharing Service but have also expanded our expertise in working with third-party APIs. We are committed to continually improving and refining these integrations to provide the best possible user experience.

---

## 🧠Potential Improvements
Our Car Sharing App has undergone extensive development and testing, but like any sophisticated system, there's always room for enhancement. Here are potential improvements that can further elevate the user experience and functionality of our platform:

1. **Enhanced User Feedback Mechanism:**

* Implement a more comprehensive feedback system for users to provide ratings and reviews for both cars and the overall rental experience.
* Integrate a notification system to inform users about the status of their feedback, fostering a sense of engagement and responsiveness.

---

2. **Intelligent Recommendation System:**

* Develop a recommendation engine that suggests cars based on user preferences, rental history, and popular choices among similar users.
* Utilize machine learning algorithms to continually improve and personalize recommendations, enhancing the overall user satisfaction and loyalty.

--- 

3. **Advanced Analytics Dashboard:**

* Create a robust analytics dashboard for managers, offering insights into key performance indicators (KPIs) such as utilization rates, popular car models, and peak usage times.
* Implement data visualization tools to make complex data easily understandable, aiding in strategic decision-making.

---

4. **Geolocation Integration:**

* Incorporate geolocation services to enable real-time tracking of rented vehicles, providing users and managers with accurate information about the car's location and estimated arrival times.
* Enhance security features by implementing geofencing for designated parking areas and alerting users if they try to end a rental outside these zones.

---

5. **Multi-Language Support:**

* Introduce multi-language support for the application to cater to a broader user base, considering the diverse linguistic preferences of our global audience.
* Implement a user-friendly language selection feature in the app settings, allowing users to choose their preferred language for a more inclusive experience.

---

6. **Streamlined Onboarding Process:**

* Simplify the user onboarding process by reducing the number of steps required to register and start using the app.
* Incorporate a guided tour or interactive tutorial for new users, highlighting key features and ensuring a smooth introduction to the platform.

## 💬Summary
In summary, our Car Sharing App stands as a reliable and efficient platform for managing vehicle rentals, offering a range of features designed to provide a seamless experience for both customers and managers. Leveraging a modern technology stack, including Spring Boot, MySQL, and various essential libraries, our app ensures robust functionality, security, and scalability.

Despite the challenges faced during development, particularly in integrating the Telegram and Stripe APIs, our team successfully overcame these hurdles, leading to valuable learning experiences and improved expertise in working with third-party services.

Looking ahead, potential improvements in user feedback, recommendation systems, analytics, geolocation integration, language support, and onboarding processes can further refine and elevate the overall user experience. Our commitment to continuous improvement and innovation positions our Car Sharing App as a dynamic and evolving solution in the realm of vehicle sharing services.

<img src="images/logo.png" style="border-radius: 20px; width: 600px">
