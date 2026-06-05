# ── Stage 1 : build ──────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ── Stage 2 : runtime ────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S planifem && adduser -S planifem -G planifem

COPY --from=build /app/target/PF-sageFemme-0.0.1-SNAPSHOT.jar app.jar

RUN chown planifem:planifem app.jar
USER planifem

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]