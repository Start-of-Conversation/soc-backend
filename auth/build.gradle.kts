dependencies {
    implementation(project(":common"))

    //feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.2.1" +
            "")
    implementation("io.github.openfeign:feign-httpclient:13.5")

    //oauth
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    //jjwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6") // or 'io.jsonwebtoken:jjwt-gson:0.12.6' for gson

    //swagger
    implementation("io.swagger.core.v3:swagger-annotations:2.2.30")
}