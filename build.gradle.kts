import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// ./gradlew clean build --parallel

plugins {
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // 통일
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/release") }
}

tasks.bootJar {
    enabled = false
}

allprojects {
    group = "toyproject.startofconversation"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/release") }
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")

    allOpen {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.MappedSuperclass")
        annotation("jakarta.persistence.Embeddable")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
        }
    }

    val querydslVersion = "5.0.0"

    tasks.processResources {
        from(rootProject.file("soc-config")) {
            into("config")
        }
    }

    dependencies {
        //kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        //mockito-kotlin
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

        //junit
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

        //spring
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-security")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
        developmentOnly("org.springframework.boot:spring-boot-devtools")

        //redis
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("io.lettuce:lettuce-core")

        //database
        runtimeOnly("org.postgresql:postgresql")

        //kotest
//        testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
//        testImplementation("io.kotest:kotest-assertions-core:5.9.1")
//        testImplementation("io.kotest:kotest-property:5.9.1")

        //swagger
        implementation("io.swagger.core.v3:swagger-annotations:2.2.30")

        //aws
        implementation(platform("software.amazon.awssdk:bom:2.32.6"))
        implementation("software.amazon.awssdk:auth")
        implementation("software.amazon.awssdk:regions")

        // querydsl
        if (name in listOf("common", "auth", "api")) {
            implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
            implementation("jakarta.persistence:jakarta.persistence-api")
            implementation("jakarta.annotation:jakarta.annotation-api")

            kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
            kapt("org.springframework.boot:spring-boot-configuration-processor")
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Jar> {
        enabled = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
