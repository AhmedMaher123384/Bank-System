# Bank Management Microservices

## Overview
This application is designed to manage banking operations through various microservices. 
It provides functionality for user authentication, account management, and transaction handling.

## Technologies Used
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications.
- **Spring Security**: For securing the application.
- **JWT (JSON Web Tokens)**: For handling authentication tokens.
- **PostgreSQL**: Relational database for storing persistent data.
- **Lombok**: To reduce boilerplate code for model objects.
- **JUnit**: Framework for unit testing Java applications.


## Features

### Auth Service
The Auth Service handles user registration, login, password changing,logout, and token refreshing.

1. **Register:** Creates a new user account if the email is not already registered.
2. **Login:** Authenticates a user and provides an access token and refresh token.
3. **Logout:** Invalidates the access and refresh tokens by blacklisting them.
4. **Change Password:** Allows users to change their password after verifying the current password.
5. **Refresh Token:** Generates a new access token using a valid refresh token.



### Account Service
The Account Service manages user accounts.

- **Create New Account:** Generates a new account for the authenticated user with a unique card number and initial balance.
- **Get My Accounts:** Retrieves all accounts associated with the authenticated user.


### Transaction Service
The Transaction Service handles deposit and withdrawal transactions.

- **Deposit:** Adds a specified amount to the user's account balance.
- **Withdraw:** Deducts a specified amount from the user's account balance, checking for sufficient funds.


### User Service
The User Service provides user profile information.

- **Get User Profile:** Retrieves the profile details of the authenticated user.

## Setup
1. Clone the repository.
2. Configure your database credentials in `enviroment in docker-compose.yml`.
3. Set up Maven:
   ```bash
   mvn clean install
4. Set up Docker and run Docker Compose:
   ```bash
   docker-compose up -d 

## Usage
Use the provided endpoints to manage authentication, accounts, and transactions through a RESTful API.
