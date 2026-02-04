FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN for i in 1 2 3; do mvn install -N -DskipTests --fail-at-end --batch-mode --errors --update-snapshots --no-transfer-progress && break || sleep 10; done

COPY commons-library commons-library
WORKDIR /app/commons-library
RUN mvn clean install -DskipTests

WORKDIR /app
COPY report-service report-service
WORKDIR /app/report-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN apk update && apk upgrade
WORKDIR /app
COPY --from=build /app/report-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
