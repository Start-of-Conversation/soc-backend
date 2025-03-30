dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR9")
    }
}

dependencies {
    implementation(project(":common"))

    //feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-httpclient:13.5")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}