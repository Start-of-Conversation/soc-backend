tasks.bootJar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(project(":common").projectDir.resolve("src/main/resources")) {
        include("application-common.yml")
    }
    from(project(":log").projectDir.resolve("src/main/resources")) {
        include("application-log.yml")
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
    implementation(project(":log"))

    //batch
    implementation("org.springframework.boot:spring-boot-starter-batch")
    testImplementation("org.springframework.batch:spring-batch-test")

    //fcm
    implementation("com.google.firebase:firebase-admin:9.5.0")

    //mail
    implementation("org.springframework.boot:spring-boot-starter-mail")
}