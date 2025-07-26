
tasks.bootJar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
}
