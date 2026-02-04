FROM public.ecr.aws/docker/library/maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -DskipTests --fail-at-end --batch-mode --errors --update-snapshots --no-transfer-progress

COPY commons-library commons-library
WORKDIR /app/commons-library
RUN mvn clean install -DskipTests

WORKDIR /app
COPY audit-service audit-service
WORKDIR /app/audit-service
RUN mvn clean package -DskipTests

FROM public.ecr.aws/docker/library/eclipse-temurin:21-jre-alpine
RUN apk update && apk upgrade
WORKDIR /app
COPY --from=build /app/audit-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
