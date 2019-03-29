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
