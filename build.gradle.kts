plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.molniya"
version = "0.0.1-SNAPSHOT"
description = "molniya_backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // --- Spring Boot ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // --- Flyway for DB migrations ---
    implementation("org.flywaydb:flyway-core")
    // поддержка PostgreSQL-specific миграций (например, типов)
    implementation("org.flywaydb:flyway-database-postgresql")

    // --- PostgreSQL driver ---
    runtimeOnly("org.postgresql:postgresql")

    // --- JWT ---
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // --- Email ---
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // --- Dev & Tools ---
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    // --- Testing ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // --- Monitoring & Observability ---
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // --- OpenTelemetry for tracing ---
//    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.45.0")
//    implementation("io.micrometer:micrometer-tracing")
//    implementation("io.micrometer:micrometer-tracing-bridge-otel")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
