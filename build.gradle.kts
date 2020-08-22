plugins {
    java
}

group = "com.expansemc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo-new.spongepowered.org/repository/maven-public/")
    maven("https://repo.spongepowered.org/maven")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}