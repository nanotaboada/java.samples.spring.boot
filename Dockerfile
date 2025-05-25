# ------------------------------------------------------------------------------
# Stage 1: Builder
# This stage builds the application and its dependencies.
# ------------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies. This will be cached until pom.xml
# changes, speeding up the build process.
COPY pom.xml    .
RUN mvn dependency:go-offline -B

# Copy source code and build the application, skipping tests for faster builds.
COPY src        ./src
RUN mvn clean package -DskipTests

# ------------------------------------------------------------------------------
# Stage 2: Runtime
# This stage creates the final, minimal image to run the application.
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS runtime

WORKDIR /app

# Install curl for health check
RUN apk add --no-cache curl

# Metadata labels for the image. These are useful for registries and inspection.
LABEL org.opencontainers.image.title="🧪 RESTful Web Service with Spring Boot"
LABEL org.opencontainers.image.description="Proof of Concept for a RESTful Web Service made with JDK 21 (LTS) and Spring Boot 3"
LABEL org.opencontainers.image.licenses="MIT"
LABEL org.opencontainers.image.source="https://github.com/nanotaboada/java.samples.spring.boot"

# https://rules.sonarsource.com/docker/RSPEC-6504/

# Copy application JAR file from the builder stage
COPY --from=builder     /app/target/*.jar           ./app.jar

# Copy metadata docs for container registries (e.g.: GitHub Container Registry)
COPY --chmod=444        README.md                   ./
COPY --chmod=555        assets/                     ./assets/

# Copy entrypoint and healthcheck scripts
COPY --chmod=555        scripts/entrypoint.sh       ./entrypoint.sh
COPY --chmod=555        scripts/healthcheck.sh      ./healthcheck.sh

# Add system user
RUN addgroup -S spring && \
    adduser -S -G spring spring

USER spring

EXPOSE 9000
EXPOSE 9001

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
    CMD ["./healthcheck.sh"]

ENTRYPOINT ["./entrypoint.sh"]
CMD ["java", "-jar", "./app.jar"]
