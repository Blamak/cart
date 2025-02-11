### Building and running the application
- Run the Application from Maven (pom.xml)
. Prerequisites
  Install Java 17 (if not already installed)
  Install Maven (if not already installed)
    Clone or navigate to your project directory.
. Steps
  Step 1. Navigate to the Project Directory:
            cd /path/to/your/project
  Step 2. Build the Project
          Compile and package the application into a JAR file:
            mvn clean package
            (This generates a JAR file inside the target/ directory)
  Step 3. Run the Application
          Use Maven to execute the Spring Boot app:
            mvn spring-boot:run
          Alternatively, run the JAR directly:
            java -jar target/cart-0.0.1-SNAPSHOT.jar
            Expected Outcome:
              The application starts, and logs appear in the console.
              The API is accessible at: http://localhost:9191/
  
🚀 Run the Spring Boot Application with Docker
📌 Prerequisites
Docker installed and running (docker --version to verify)
The project is built and packaged as a JAR file (mvn clean package)
Step 1: Build the Docker Image
Navigate to your project's root directory (where the Dockerfile is located) and run:

sh
Copiar
Editar
docker build -t cart-app .
🔹 This creates a Docker image named cart-app.
  
Step 2: Run the Docker Container
Run the container and expose it on port 8080:

sh
Copiar
Editar
docker run -p 8080:8080 cart-app
🔹 The application should now be accessible at:
➡ http://localhost:8080/

Step 3: Run in Detached Mode (Optional)
If you want the container to run in the background:

sh
Copiar
Editar
docker run -d -p 8080:8080 --name cart-container cart-app
🔹 The container runs in detached mode, and you can check logs using:

sh
Copiar
Editar
docker logs -f cart-container
Step 4: Stop & Remove the Container
If you need to stop the container:

sh
Copiar
Editar
docker stop cart-container
To remove it completely:

sh
Copiar
Editar
docker rm cart-container


Consult Docker's [getting started](https://docs.docker.com/go/get-started-sharing/)
docs for more detail on building and pushing.
