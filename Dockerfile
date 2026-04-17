# Этап 1: сборка JAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем pom.xml и загружаем зависимости (кэшируем, чтобы не скачивать при каждом билде)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код и собираем JAR (тесты пропускаем для скорости)
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: запуск
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]