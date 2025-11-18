# ========== STAGE 1: BUILD ==========
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copio pom e sorgenti
COPY pom.xml .
COPY src ./src

# Build del jar (skip dei test se vuoi velocizzare)
RUN mvn clean package -DskipTests

# ========== STAGE 2: RUNTIME ==========
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copio il jar costruito nello stage 1
COPY --from=build /app/target/*.jar app.jar

# Porta esposta da Spring Boot (se ne usi un’altra, cambiala)
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
