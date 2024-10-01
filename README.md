# Shoply

Your one-stop shop🛒

**Shoply** is an exciting e-commerce application developed using **Java** and **Spring Boot 3**. Built on a **microservices-based architecture**, it offers scalability, fault tolerance and ease of maintenance.

---

## Architecture Overview

Shoply is composed of several independent microservices, each responsible for a specific domain of the e-commerce platform. These microservices are orchestrated using **Spring Cloud** tools.

### Microservices

1. **API Gateway**
   - **Technology**: Spring Cloud Gateway
   - **Responsibilities**: Routes incoming requests to the appropriate microservice and acts as a central entry point for external clients.
   - **Authentication**: Implements **OAuth2** using **Keycloak** to secure access to the services.

2. **Discovery Server**
   - **Technology**: Netflix Eureka
   - **Responsibilities**: Service discovery, allowing microservices to register themselves and find each other dynamically.
   - All other microservices register with this discovery server.

3. **Product Service**
   - **Technology**: MongoDB
   - **Responsibilities**: Manages product data, including creation, updates, and retrieval. Handles queries related to the catalog.
   - **Future Merge**: This service will eventually be merged with the Inventory Service.

4. **Order Service**
   - **Technology**: MySQL, Kafka, Resilience4J, Spring Cloud LoadBalancer
   - **Responsibilities**: Handles order placement, order history, and order processing.
   - **Circuit Breaker**: Uses **Resilience4J** to monitor requests within this service for order processing.
   - **Load Balancer**: Utilizes **Spring Cloud** to load balance requests to the **Inventory Service** for checking product availability.
   - **Async Communication**: Uses **Apache Kafka** to asynchronously notify the **Notification Service** when an order is placed.

5. **Inventory Service**
   - **Technology**: MySQL
   - **Responsibilities**: Manages product availability and stock levels.
   - **Future Merge**: This service will eventually be merged with the Product Service.

6. **Notification Service**
   - **Technology**: Kafka, JavaMailSender
   - **Responsibilities**: Sends email notifications when an order is placed.

---

## Key Features & Technologies

### Core Technologies
- **Java 17**
- **Spring Boot 3**
- **Maven**: Used as the build tool for dependency management and building the services.
- **Docker**: Each microservice is containerized for easy deployment and scalability.
- **Jib**: Integrated into Maven for building Docker images of the application microservices and pushing them to Docker Hub.
- **Docker Compose**: Provides the dockerized versions of third-party services and orchestrates them alongside the dockerized microservices into a cohesive application.

### Microservices Communication
- **Kafka**: Handles asynchronous communication between **Order Service** and **Notification Service**.
- **WebClient**: Handles synchronous communication between **Order Service** and **Inventory Service**.

### Distributed Tracing of Requests & Monitoring
- **Micrometer (Brave)**: Supports distributed tracing by providing instrumentation
- **Tempo**: Provides trace storage for the collected distributed traces
- **Zipkin**: Offers visualization of the distributed traces
- **Spring Boot Actuator**: Exposes endpoints for application health, metrics, and other monitoring features.
- **Prometheus & Grafana**: Collects and displays application performance metrics.
- **Loki**: Aggregates logs for all microservices for centralized logging and troubleshooting.

### Security
- **Keycloak**: Provides OAuth2-based authentication for securing API access via the **API Gateway**.

### Circuit-Breaking
- **Resilience4J**: Monitors the availability of the microservice and opens the circuit to prevent overwhelming the service with requests when it is under stress or unreachable.

### Testing
- **TestContainers**, **JUnit**, **MockMVC**

---

## Setup Instructions

### Step 1: Clone the Repository
Clone the repository to your local machine:

```bash
git clone https://github.com/Kagimbz/shoply.git
cd shoply
```

### Step 2: Configure Keycloak
1. Create a realm named **shoply-realm** in Keycloak and add the necessary configuration.
2. Export the realm data and replace the existing JSON file in the `api-gateway/keycloak-realms` directory. Optionally, you can use the provided one (this way, you won't need to configure Keycloak).

### Step 3: Configure the Notification Service
Ensure the **Notification Service** is configured with your SMTP server details and sender information (inside its `application.properties` file) for email notifications.

### Step 4: Configure Docker Images
To reflect your Docker Hub repository for Jib, modify the parent project’s `pom.xml` file under:
- `project > build > plugins > plugin > configuration > to > image`

Change the value to:

```xml
registry.hub.docker.com/<your-docker-username>/${project.artifactId}
```
Then, build and push the microservices' Docker images using the following command:

```bash
mvn compile jib:build
```

### Step 5: Run with Docker Compose
To bring up the entire microservices ecosystem and their third-party services, run:

```bash
docker-compose up -d
```

---

## Ports
Each service is exposed on specific ports when using Docker Compose:

- **Keycloak**: `localhost:8080`
- **API Gateway**: `localhost:8181` (for accessing APIs, e.g. `/api/v1/products` and `/api/v1/orders`)
- **Discovery Server**: `localhost:8761`
- **Inventory Service**: `localhost:8082` (Temporary port until merged with Product Service)
- **Zipkin**: `localhost:9411`
- **Prometheus**: `localhost:9090`
- **Grafana**: `localhost:3000`
- **Loki**: `localhost:3100`
- **Tempo**: `localhost:3110`
- **Kafka Broker**: `localhost:9092`
- **Kafka Zookeeper**: `localhost:2181`
- **Mysql (Order service)**: `localhost:3307`
- **Mysql (Inventory service)**: `localhost:3308`
- **Mysql (Keycloak)**: `localhost:3309`
- **MongoDB (Product service)**: `localhost:27017`

---

## Coming Soon
- **Cart Service**: Uses Redis as a cache service for user carts.
- **Payment Service**: Integrates with **Daraja (Mpesa)** for mobile payments and **Stripe** for credit/debit card payments.

---

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Show Some Love!
If you like this project, please consider giving it a star ⭐! Your support keeps the magic alive!
