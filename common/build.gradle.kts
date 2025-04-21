import org.springframework.boot.gradle.tasks.bundling.BootJar

val bootJar: BootJar by tasks
bootJar.enabled = false

tasks.named<Jar>("jar") {
    enabled = true // jar 작업 활성화
}

dependencies {
}