# Backend-Service - API Service for employee and task management

## Table of Contents
- [Table of Contents](#table-of-contents)
- [Description](#description)
- [Technology](#technology)
- [Feature](#feature)
- [Installation](#installation)

## Description
This is a Spring Boot application that provides functionality for managing accounts, handling authentication, and managing tasks.

## Technology
- Java 17
- Spring Boot 3.3
- MySQL
- Docker

## Feature
1. Account Management
- Handles CRUD operations for user accounts.
2. Authencation
- Handles user authentication and authorization
3. Task Management
- Handles task creation, assignment, and tracking status.

## Installation
1. Get the latest source code
2. Open terminal, go to project folder and build application
```bash
mvn clean package
```
3. Run application
```bash
docker-compose -f docker-compose.yml up -d --build
```
4. Access the application
```bash
http://localhost:8080/backend-service/swagger-ui/index.html
```
5. Stop the application
```bash
docker-compose down -v
```