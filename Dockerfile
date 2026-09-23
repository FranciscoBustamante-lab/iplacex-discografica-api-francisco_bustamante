# Etapa 1: compilar con Gradle y Java 21
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle clean bootWar --no-daemon

# Etapa 2: ejecutar con OpenJDK 21
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/build/libs/discografia-1.war app.war

ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]