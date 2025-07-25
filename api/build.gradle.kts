springBoot {
    mainClass.set("toyproject.startofconversation.api.ApiApplicationKt")
}

tasks.bootJar {
    enabled = true
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(project(":common").projectDir.resolve("src/main/resources")) {
        include("application-common.yml")
    }
    from(project(":log").projectDir.resolve("src/main/resources")) {
        include("application-log.yml")
    }
    from(project(":auth").projectDir.resolve("src/main/resources")) {
        include("application-auth.yml")
    }
    from(project(":notification").projectDir.resolve("src/main/resources")) {
        include("application-notification.yml")
    }
    from(project(":notification").projectDir.resolve("src/main/resources/key")) {
        include("firebase-service-account.json")
        into("key")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":log"))
    implementation(project(":auth"))
    implementation(project(":notification"))

    //security
    implementation("org.springframework.security:spring-security-crypto")

    //S3
    implementation("software.amazon.awssdk:s3")
}
