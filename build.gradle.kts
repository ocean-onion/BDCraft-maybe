plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"
description = "A simple Paper plugin"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Use Paper 1.20.4 API which is compatible with Java 17
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.mockito:mockito-core:5.8.0")
}

java {
    // Accept Java 17 or higher
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    // Set character encoding
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(project.name)
        mergeServiceFiles()
    }
    
    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
