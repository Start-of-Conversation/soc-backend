springBoot {
    mainClass.set("toyproject.startofconversation.api.ApiApplicationKt")
}

tasks.bootJar {
    enabled = true
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

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
