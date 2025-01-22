# Use Maven as the build environment
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java","-jar","backend-service.jar"]

EXPOSE 80