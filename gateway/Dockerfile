FROM public.ecr.aws/docker/library/maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -DskipTests --fail-at-end --batch-mode --errors --update-snapshots --no-transfer-progress

COPY commons-library commons-library
WORKDIR /app/commons-library
RUN mvn clean install -DskipTests

WORKDIR /app
COPY gateway gateway
WORKDIR /app/gateway
RUN mvn clean package -DskipTests

FROM public.ecr.aws/docker/library/eclipse-temurin:21-jre-alpine
WORKDIR /app
# Upgrade OS packages to fix vulnerabilities (libpng, etc.)
RUN apk update && apk upgrade
COPY --from=build /app/gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
