import org.gradle.util.GradleVersion

rootProject.name = "bdcraft"

// Set project Gradle version
gradle.rootProject {
    val gradleVersion = "8.14"
    if (GradleVersion.current() < GradleVersion.version(gradleVersion)) {
        throw GradleException("This project requires Gradle $gradleVersion or later. Current version: ${gradle.gradleVersion}")
    }
}
