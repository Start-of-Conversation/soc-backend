tasks.bootJar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(project(":common"))
    implementation(project(":log"))

//    //batch
//    implementation("org.springframework.boot:spring-boot-starter-batch")
//    testImplementation("org.springframework.batch:spring-batch-test")

    //fcm
    implementation("com.google.firebase:firebase-admin:9.5.0")

    //mail
    implementation("org.springframework.boot:spring-boot-starter-mail")
}