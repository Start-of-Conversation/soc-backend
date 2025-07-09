
tasks.bootJar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(project(":common").projectDir.resolve("src/main/resources")) {
        include("application-common.yml")
    }
}

sourceSets {
    test {
        resources {
            srcDir("src/main/resources")
        }
    }
}

dependencies {
}
