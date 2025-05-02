dependencies {
    implementation(project(":common"))
    implementation(project(":auth"))

    //security
    implementation("org.springframework.security:spring-security-crypto")

    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
}
