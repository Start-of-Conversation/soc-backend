import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val bootJar: BootJar by tasks
bootJar.enabled = false

tasks.named<Jar>("jar") {
    enabled = true // jar 작업 활성화
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    runtimeOnly ("org.postgresql:postgresql")
    testImplementation ("org.springframework.security:spring-security-test")
}