plugins {
    java
    id("io.quarkus")
    id("checkstyle")
    id("org.sonarqube") version "4.4.1.3373"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-amazon-lambda-http")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-h2")
    testImplementation("io.quarkus:quarkus-jacoco")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "broccoli"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

extensions.getByType(CheckstyleExtension::class.java).apply {
    toolVersion = "10.12.5"
    enableExternalDtdLoad = true
}

sonar {
    properties {
        property("sonar.projectKey", "miguoliang_broccoli-quarkus")
        property("sonar.organization", "miguoliang")
        property("sonar.host.url", "https://sonarcloud.io")
//        property("sonar.projectBaseDir", layout.projectDirectory.asFile.absolutePath)
//        property("sonar.java.binaries", layout.buildDirectory.dir("classes").get().asFile.absolutePath)
//        property("sonar.coverage.jacoco.xmlReportPaths", layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath)
//        property("sonar.sources", layout.projectDirectory.dir("src/main").asFile.absolutePath)
//        property("sonar.tests", layout.projectDirectory.dir("src/test").asFile.absolutePath)
    }
}