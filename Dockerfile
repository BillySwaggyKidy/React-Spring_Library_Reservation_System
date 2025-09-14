FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Installer Node.js + npm
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests -Pdemo

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]