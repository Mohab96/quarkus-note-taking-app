# CRUD Note Taking App using Quarkus

## Introduction

This is a RESTful API for a simple note taking application (like google keep). It is built using Quarkus a modern Java
framework, It implements
CRUD (Create, Retrieve, Update and Delete) operations for notes and tags. It also implements validation and error
handling all incoming data for all API endpoints.

## Getting Started

To get started, follow these steps:

1. Clone the repository.
2. Create a new database in your local PostgreSQL server.
3. Execute the sql script `schema.sql` in the database to create the necessary tables.
4. Run this command to start the application:

```shell script
./mvnw compile quarkus:dev
```

5. You will be prompted to enter the database connection details, enter the details and the application will start. Use
   these details as guides to be able to fill the data needed to you when prompted:
    - `quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}`
    - `quarkus.datasource.username=${DB_USER}`
    - `quarkus.datasource.password=${DB_PASSWORD}`
    - `quarkus.datasource.db-kind=postgresql`
6. You can now access the application at `http://localhost:8080`.
7. You can use the Swagger UI to interact with the API at `http://localhost:8080/q/swagger-ui/`.
8. You can also use the Dev UI to interact with the application at `http://localhost:8080/q/dev/`.
9. You can also use
   this [Postman workspace](https://www.postman.com/science-engineer-87747892/workspace/quarkusnotetakingapi) to
   interact with the API directly from Postman.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`
