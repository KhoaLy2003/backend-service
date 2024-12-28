FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java","-jar","backend-service.jar"]

EXPOSE 80