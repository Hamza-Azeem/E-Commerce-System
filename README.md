# **E-Commerce Management System Documentation**

## **Getting Started**

To run the application using Spring Boot and MySQL, follow these steps:

1. **Clone the Repository:**
   - Clone the project from GitHub to your local machine.

2. **Configure Database Connection:**
   - Open the project directory in your favorite IDE.
   - Navigate to the `application.properties` file.
   - Update the following properties with your MySQL properties:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your_schema_name
     spring.datasource.username=your-database-username
     spring.datasource.password=your-database-password
     ```

3. **Run the Application:**
   - Start the application using your IDE's run configuration or by executing the command:
     ```bash
     mvn spring-boot:run
     ```

After completing these steps, the application should be up and running.

## **Application Architecture**

The application initially followed a monolithic architecture, where all components were integrated into a single system. As the project evolved, the email service was refactored using a microservices architecture, enhancing modularity and scalability in handling email communications with users.

### **Microservice Architecture**

- **Configuration Server**:
    - Centralized location where the configuration of all services is managed, enabling easier modifications and preventing decoupling.

- **Discovery Server**:
    - Allows all microservices in the project to register themselves, facilitating communication and enabling services to discover one another.

- **API Gateway**:
    - Acts as an entry point for all incoming requests, handling authentication and authorization while routing requests to the appropriate services. It also implements the circuit breaker pattern for resilience.

- **Email Service**:
    - A dedicated service responsible for sending emails to users of the e-commerce application.

- **E-Commerce**:
    - An N-tier application that handles user information, products, categories, vouchers, and carts.

## **Application Initialization**

- Upon the first start of the application, a default user is created:
  - **Email:** `admin@example.com`
  - **Password:** `admin`
  - **Role:** `ADMIN`

## **Security and Authorization**

The application integrates role-based security. The roles and their corresponding access levels are as follows:

- **MANAGER:**
  - Can access all endpoints without restrictions.
  - You can create a role named `MANAGER` in the `role` table and assign it to a user.

- **ADMIN:**
  - Can access all `POST`, `PUT`, and `GET` endpoints.
  - Cannot access `DELETE` endpoints.
  - The `admin` user has this role by default.

- **USER:**
  - Can access only the `GET` endpoints.

- **Public Endpoints:**
  - `GET /auth/register`

## **Testing Application Endpoints with Swagger**

The application includes Swagger for easy testing and interaction with the API endpoints. Swagger provides a user-friendly interface that allows you to explore, test, and document the API without the need for external tools like Postman.

### **How to Access Swagger UI**

1. **Run the Application:**
   - Ensure that the application is running. Follow the steps outlined in the "Getting Started" section to start the application.

2. **Open Swagger UI:**
   - Open your web browser and navigate to the following URL:
     ```
     http://localhost:8080/swagger-ui.html
     ```
   - This will bring up the Swagger UI, which automatically documents and displays all available endpoints in your application.

### **Using Swagger UI**

- **Explore Endpoints:**
  - The Swagger UI lists all available endpoints, grouped by their respective controllers. Each endpoint is displayed with its HTTP method (GET, POST, PUT, DELETE) and a brief description.

- **Test Endpoints:**
  - You can test any endpoint directly from the Swagger UI:
    1. Select the endpoint you want to test.
    2. Click on the endpoint to expand its details.
    3. Enter the required parameters in the input fields provided.
    4. Click the **"Try it out"** button to execute the request.
    5. Review the response returned by the server, which includes status codes, response body, and headers.

- **View Models:**
  - Swagger also displays the data models used in requests and responses. This includes details about the structure of the data, required fields, and example values.

### **No Authentication Required**

- All endpoints in Swagger are publicly accessible, so you can interact with any of them without needing to provide authorization or authentication tokens.

## **Data Storage**

The application uses **MySQL** as the database to persist details about products, categories, users, carts, cartItems and vouchers. Relationships between entities are properly set up to maintain data integrity.

## **Validation and Error Handling**

- All API requests undergo input validation to ensure data correctness.
- In case of validation failures or exceptions, appropriate HTTP status codes and error messages are returned to the client.

## **Authentication and Authorization**

- The application has integrated security features that enforce role-based authentication and authorization.

## **Caching**

- A caching mechanism is implemented to ensure fast data retrieval.

## **Contact**

- **Name:** Hamza Azeem
- **Email:** hamzaalsherif9@gmail.com
