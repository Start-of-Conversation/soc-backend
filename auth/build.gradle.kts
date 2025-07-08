tasks.bootJar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(project(":common").projectDir.resolve("src/main/resources")) {
        include("application-common.yml")
    }
    from(project(":notification").projectDir.resolve("src/main/resources")) {
        include("application-notification.yml")
    }
}

sourceSets {
    test {
        resources {
            srcDir("src/main/resources")
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":notification"))

    //feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.3.0")

    //oauth
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    //jjwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6") // or 'io.jsonwebtoken:jjwt-gson:0.12.6' for gson

    //swagger
    implementation("io.swagger.core.v3:swagger-annotations:2.2.30")
}