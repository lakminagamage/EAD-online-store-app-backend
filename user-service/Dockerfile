# Build
FROM openjdk:21-jdk-slim AS build
RUN apt-get update && apt-get install -y \
    maven \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
