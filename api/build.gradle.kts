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
    from(project(":auth").projectDir.resolve("src/main/resources")) {
        include("application-auth.yml")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":auth"))

    //security
    implementation("org.springframework.security:spring-security-crypto")

    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
}
