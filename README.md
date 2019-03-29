# BankingAPI

This application was generated using JHipster 5.8.2.

On launch, it will refuse to start if it is not able to connect to PostgreSQL container.

## Development

To start your application in the dev profile, simply run:

    ./mvnw

## Building for production

To optimize the BankingAPI application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war

## Running in production

Package:

    ./mvnw package -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d
    
## Requirements met

This application:
- was loaded with multiple users (user, user2, admin) and the passwords are equal to the username.

- also loaded with sample data, check /resources/config/liquibase/*.csv for details.

- allows the authentication of a user using JWT
    - POST /BankingAPI/api/authenticate with {"username":"user", "password": "user"}
    - use the token received in all the following calls to the API
    
- lists all the bank accounts for the current user
    - GET /BankingAPI/api/bank-accounts
    
- creates a payment
    - POST /BankingAPI/api/payments with 
    `{
     	"giverAccount":"LU280019400644750000",
         "amount": 10000.0,
         "currency": "EUR",
         "beneficiaryAccountNumber": "LU755327465763654871",
         "beneficiaryName": "Beneficiary Name",
         "communication": "TRF04"
     }`
     
- lists all payments for the current user
    - GET /BankingAPI/api/payments
    
- deletes a payment from the current user by id
    - DELETE /BankingAPI/api/payments/{id}
    
- updates user info (address, password)
    - PUT /BankingAPI/api/customers with 
    `{
     	"currentPassword": "user",
     	"newPassword": "newuser",
     	"address": "Another Address"
     }`

This application also meets the following optional requirements:
- passwords are encrypted with BCrypt;
- listing all payments returns a pageable response 
    - Ex: `/BankingAPI/api/payments?page=0&size=2` returns only the first 2 payments
- runs on a docker architecture
- uses Liquibase as a database migration framework


This application does NOT:
- have a logout endpoint
- enforces the full payment execution as a single transaction
- have a good test coverage
- have Postman integration tests
- register fraud tentatives
- separates concerns into multiple microservices
- enforces password policies
- provide swagger specs
- allows users to upload "photos"
- enables CORS (but CSRF is disabled)
