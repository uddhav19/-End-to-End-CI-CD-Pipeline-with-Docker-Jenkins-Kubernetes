# Stage 1 - Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package 

# Stage 2 - Distroless Runtime
FROM gcr.io/distroless/java17

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Run as non-root (distroless provides this user)
USER nonroot:nonroot

ENTRYPOINT ["java","-jar","app.jar"]
