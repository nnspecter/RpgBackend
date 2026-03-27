# Этап 1: Сборка
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Копируем pom.xml и скачиваем зависимости (кеширование)
COPY pom.xml .
RUN mvn dependency:go-offline
# Копируем исходный код и собираем .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Копируем только собранный jar из первого этапа
COPY --from=build /app/target/*.jar app.jar
# Порт, на котором работает Spring (по умолчанию 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]