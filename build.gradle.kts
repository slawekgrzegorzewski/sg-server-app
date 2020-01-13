import java.nio.file.Paths;

plugins {
    id("org.springframework.boot") version "2.1.6.RELEASE" apply true
    java apply true
    kotlin("jvm") version "1.3.50" apply true
}

group = "pl.sg"
version = "0.0.1-SNAPSHOT"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configurations["compileOnly"].extendsFrom(configurations["annotationProcessor"])

repositories {
    mavenCentral()
}

dependencies {

    configurations.implementation.get().isCanBeResolved = true;
    implementation("org.springframework.boot:spring-boot:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-quartz:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.6.RELEASE")
    implementation("org.springframework.session:spring-session-core:2.1.7.RELEASE")
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.1.6.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-client:5.1.5.RELEASE")
    implementation(kotlin("stdlib-jdk8"))

    compileOnly("org.projectlombok:lombok:1.18.8")
    compileOnly("org.springframework.boot:spring-boot-devtools:2.1.6.RELEASE")

    runtime("org.postgresql:postgresql:42.2.6")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.1.6.RELEASE")
    annotationProcessor("org.projectlombok:lombok:1.18.8")

    testImplementation("junit:junit:4.12")
    testImplementation("org.springframework:spring-test:4.3.2.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-test:2.1.6.RELEASE")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("com.jayway.jsonpath:json-path:2.2.0")
    testRuntime("org.springframework.security:spring-security-test:5.1.5.RELEASE")
}

tasks.clean.get().doFirst {
    val dockerDir = Paths.get(project.rootDir.absolutePath, "docker", "development");
    delete(dockerDir.resolve("application.yml"))
    delete(dockerDir.resolve("accountant.jar"))
}

val jar by tasks.getting(Jar::class);
tasks.register<Copy>("toDocker") {
    dependsOn.add(tasks.build)
    group = "docker"
    from(jar.archiveFile){
        rename { "accountant.jar" }
    }
    from(Paths.get(project.rootDir.absolutePath, "src", "main", "resources", "application-dev-docker.yml")) {
        rename { "application.yml" }
    }
    destinationDir = Paths.get(project.rootDir.absolutePath, "docker", "development").toFile()
}

apply(from = "$rootDir/integrationTest.gradle.kts")