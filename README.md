## **📝 API Documentation - Swagger UI 🎛️**
This application includes **Swagger UI** for API documentation and testing.  
Once the application is running, access Swagger UI at:  
➡ **[http://localhost:9191/swagger-ui/index.html](http://localhost:9191/swagger-ui/index.html)**
---

# 🚀 How to Run this Spring Boot Application

This guide explains how to execute the application **from Maven** 🛠️ and **using Docker** 🐳.

---

## **1️⃣ Run the Application from Maven (`pom.xml`) 🛠️**
This project uses **Spring Boot**, so you can run it using Maven.

### **📌 Prerequisites** ⚙️
- ✅ **Java 17** installed  
- ✅ **Maven** installed  
- ✅ Clone or navigate to the project directory

### **🛠️ Steps to Run**
#### **🗂️ Step 1: Navigate to the Project Directory**
```sh
cd /path/to/the/project
```

#### **🔨 Step 2: Build the Project**
Compile and package the application into a **JAR file**:
```sh
mvn clean package
```
🎯 This generates a JAR file inside the `target/` directory.

#### **🚀 Step 3: Run the Application**
Use **Maven** to execute the Spring Boot app:
```sh
mvn spring-boot:run
```
Alternatively, run the JAR directly:
```sh
java -jar target/cart-0.0.1-SNAPSHOT.jar
```

### **🎯 Expected Outcome**
- ✅ The application starts, and logs appear in the console.
- ✅ The API is accessible at: **`http://localhost:8080/`**

---

## **2️⃣ Run the Application with Docker 🐳**
The project already contains a `Dockerfile`, so you can directly build and run it.

### **📌 Prerequisites** ⚓
- ✅ **Docker installed** and running  
- ✅ The project is **built and packaged** as a JAR file (`mvn clean package`)

### **🐳 Steps to Run in Docker**
#### **📦 Step 1: Build the Docker Image**
Navigate to your project's root directory (where the `Dockerfile` is located) and run:
```sh
docker build -t cart-app .
```
🔹 This creates a Docker image named **cart-app**.

#### **🚢 Step 2: Run the Docker Container**
Run the container and expose it on port **9191**:
```sh
docker run -p 9191:9191 cart-app
```
🔹 The application should now be accessible at:  
🌍 **http://localhost:9191/**

#### **🌌 Step 3: Run in Detached Mode (Optional)**
If you want the container to run in the background:
```sh
docker run -d -p 9191:9191 --name cart-container cart-app
```
🔹 The container runs in **detached mode**, and you can check logs using:
```sh
docker logs -f cart-container
```

#### **🛑 Step 4: Stop & Remove the Container**
If you need to stop the container:
```sh
docker stop cart-container
```
To remove it completely:
```sh
docker rm cart-container
```

---

## **⚡ Quick Reference Table**
| 🏷️ Action | ⌨️ Command |
|-----------|-------------|
| **🔨 Build the Project** | `mvn clean package` |
| **🚀 Run with Maven** | `mvn spring-boot:run` |
| **📦 Run JAR directly** | `java -jar target/cart-0.0.1-SNAPSHOT.jar` |
| **🐳 Build Docker Image** | `docker build -t cart-app .` |
| **🚢 Run Container (Foreground)** | `docker run -p 8080:8080 cart-app` |
| **🌌 Run Container (Detached Mode)** | `docker run -d -p 8080:8080 --name cart-container cart-app` |
| **📜 Check Logs** | `docker logs -f cart-container` |
| **🛑 Stop Container** | `docker stop cart-container` |
| **🗑️ Remove Container** | `docker rm cart-container` |

