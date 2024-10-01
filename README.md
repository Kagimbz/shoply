# Shoply

Welcome to **Shoply**, an e-commerce platform developed using **Java** and **Spring Boot 3**. The platform is built with a **microservices-based architecture** for scalability, fault tolerance, and ease of maintenance.

---

## Architecture Overview

Shoply is composed of several independent microservices, each responsible for a specific domain of the e-commerce platform. These microservices communicate asynchronously and are orchestrated using **Spring Cloud** tools.

### Microservices

1. **API Gateway**
   - **Technology**: Spring Cloud Gateway
   - **Responsibilities**: Routes incoming requests to the appropriate microservice and acts as a central entry point for external clients.
   - **Authentication**: Implements **OAuth2** using **Keycloak** to secure access to the services.

2. **Discovery Server**
   - **Technology**: Netflix Eureka
   - **Responsibilities**: Service discovery, allowing microservices to register themselves and find each other dynamically.
   - All other microservices register to this discovery server.

3. **Product Service**
   - **Technology**: Spring Boot, MongoDB
   - **Responsibilities**: Manages product data, including creation, updates, and retrieval. Handles queries related to the catalog.
   - **Future Merge**: This service will eventually be merged with the Inventory Service.

4. **Order Service**
   - **Technology**: Spring Boot, MySQL, Kafka, Resilience4J
   - **Responsibilities**: Handles order placement, order history, and order processing.
   - **Circuit Breaker & Load Balancer**: Uses **Resilience4J** to balance and monitor requests to the **Inventory Service** to check product availability.
   - **Async Communication**: Uses **Apache Kafka** to asynchronously notify the **Notification Service** when an order is placed.

5. **Inventory Service**
   - **Technology**: Spring Boot, MySQL
   - **Responsibilities**: Manages product availability and stock levels.

6. **Notification Service**
   - **Technology**: Spring Boot, Mail Server (SMTP)
   - **Responsibilities**: Sends email notifications when an order is placed.

---

## Key Features & Technologies

### Core Technologies
- **Spring Boot 3**: Provides a lightweight, modular framework for building Java-based microservices.
- **Maven**: Used as the build tool for dependency management and building the services.
- **Docker**: Each microservice is containerized for easy deployment and scalability.
- **Docker Compose**: Orchestrates the microservices and third-party dependencies into a cohesive application.
- **Jib**: Integrated into Maven for building Docker images and pushing them to Docker Hub without requiring a Dockerfile.

### Microservices Communication
- **Kafka**: Ensures reliable asynchronous communication between services, specifically used between the **Order Service** and **Notification Service**.
- **Resilience4J**: Provides circuit breaker functionality for handling faults and load balancing in the **Order Service**.

### Distributed Tracing & Monitoring
- **Micrometer (Brave)**: Collects application metrics for distributed tracing.
- **Tempo**: Trace storage.
- **Zipkin UI**: Provides a visualization of distributed traces for better debugging and analysis.
- **Spring Boot Actuator**: Exposes endpoints for application health, metrics, and other monitoring features.
- **Prometheus & Grafana**: Collects and displays application performance metrics.
- **Loki**: Aggregates logs for all microservices for centralized logging and troubleshooting.

### Security
- **Keycloak**: Provides OAuth2-based authentication for securing API access via the **API Gateway**.

---

## Setup Instructions

### Step 1: Clone the Repository
Clone the repository to your local machine:

```bash
git clone https://github.com/<your-username>/shoply.git
cd shoply
