# =========================
# 1️⃣ BUILD ANGULAR
# =========================
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend

COPY frontend/package*.json ./
RUN npm install

COPY frontend/ .
RUN npm run build --prod

# =========================
# 2️⃣ BUILD SPRING BOOT
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app/backend

COPY backend/pom.xml .
RUN mvn dependency:go-offline

COPY backend/ .
RUN mvn clean package -DskipTests

# =========================
# 3️⃣ RUNTIME
# =========================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR
COPY --from=backend-build /app/backend/target/*.jar app.jar

# Copiamos Angular al static de Spring
COPY --from=frontend-build /app/frontend/dist /app/static

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
