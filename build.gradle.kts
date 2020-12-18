import java.nio.file.Path
import java.nio.file.Paths

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE" apply true
    java apply true
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

    implementation("com.auth0:java-jwt:3.10.3")
    implementation("com.google.code.gson:gson:2.8.6")

    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.springframework.boot:spring-boot:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-quartz:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.3.3.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.3.3.RELEASE")
    implementation("org.hibernate.validator:hibernate-validator:6.1.5.Final")
    implementation("org.codehaus.groovy:groovy-all:3.0.7")

    implementation("org.modelmapper:modelmapper:2.3.6")
    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("com.sendgrid:sendgrid-java:4.7.0")

    compileOnly("org.projectlombok:lombok:1.18.8")
    compileOnly("org.springframework.boot:spring-boot-devtools:2.3.3.RELEASE")

    runtime("org.postgresql:postgresql:42.2.6")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.3.3.RELEASE")
    annotationProcessor("org.projectlombok:lombok:1.18.8")

    testImplementation("junit:junit:4.12")
    testImplementation("org.springframework:spring-test:4.3.2.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-test:2.3.3.RELEASE")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("com.jayway.jsonpath:json-path:2.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.1.RELEASE")
    testImplementation("io.rest-assured:rest-assured:4.3.0")

    testRuntime("org.springframework.security:spring-security-test:5.1.5.RELEASE")
}


tasks.clean.get().doFirst {
    var dockerDir = Paths.get(project.rootDir.absolutePath, "docker", "production", "raspberry");
    cleanDockerDir(dockerDir)
    dockerDir = Paths.get(project.rootDir.absolutePath, "docker", "production", "pc");
    cleanDockerDir(dockerDir)
}

fun Build_gradle.cleanDockerDir(dockerDir: Path) {
    delete(dockerDir.resolve("application.yml"))
    delete(dockerDir.resolve("accountant.jar"))
    delete(dockerDir.resolve("data.sql"))
}


tasks.withType<Jar> {
    exclude(
            "ovh",
            "application.yml",
            "application-pc-docker.yml",
            "application-raspberry-docker.yml",
            "data.sql")
}

val jar by tasks.getting(Jar::class);

tasks.register<Copy>("toDockerRaspberry") {
    dependsOn.add(tasks.build)
    group = "docker"
    from(jar.archiveFile) {
        rename { "accountant.jar" }
    }
    from(Paths.get(project.rootDir.absolutePath, "src", "main", "resources", "application-raspberry-docker.yml")) {
        rename { "application.yml" }
    }
    from(Paths.get(project.rootDir.absolutePath, "src", "main", "resources", "data.sql")) {
        rename { "data.sql" }
    }
    destinationDir = Paths.get(project.rootDir.absolutePath, "docker", "production", "raspberry").toFile()
}

tasks.register<Copy>("toDockerPC") {
    dependsOn.add(tasks.build)
    group = "docker"

    val appProject = Paths.get(project.rootDir.absolutePath, "src", "main", "resources")
    val clientProject = Paths.get(project.rootDir.absolutePath, "..", "sg-client-app")
    val destination = Paths.get("E:", "docker")

    doFirst {
        delete("${destination}")
    }

    doLast {
        val files = pl.sg.build.FilesToConvertingToUnixFilter(listOf("yml", "Dockerfile", "sql", "sh", "json", "conf"))
                .textFiles(destination)
        val convertToUnixLineEndings = pl.sg.build.ConvertToUnixLineEndings()
        files.forEach { file -> convertToUnixLineEndings.forFile(file) }
        convertToUnixLineEndings.convert();

        pl.sg.build.GenerateJwtToken().forFile(destination.resolve("docker-compose.yml")).convert()
    }

    from(jar.archiveFile) {
        rename { "backend/accountant.jar" }
    }
    from(appProject.resolve("application-pc-docker.yml")) {
        rename { "backend/application.yml" }
    }
    from(appProject.resolve("data.sql")) {
        rename { "backend/data.sql" }
    }
    from(clientProject.resolve("Dockerfile")) {
        rename { "client/Dockerfile" }
    }
    from(clientProject.resolve("angular.json")) {
        into("client")
    }
    from(clientProject.resolve("nginx.conf")) {
        into("client")
    }
    from(clientProject.resolve("package.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.app.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.base.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.json")) {
        into("client")
    }
    from(clientProject.resolve("src")) {
        into("client/src")
    }
    from(Paths.get(project.rootDir.absolutePath, "docker", "production", "pc"))
    destinationDir = destination.toFile()
}

tasks.register<Copy>("toOVH") {
    dependsOn.add(tasks.build)
    group = "docker"

    val appProject = Paths.get(project.rootDir.absolutePath, "src", "main", "resources")
    val clientProject = Paths.get(project.rootDir.absolutePath, "..", "sg-client-app")
    val destination = Paths.get("E:", "docker")

    doFirst {
        delete("${destination}")
    }

    doLast {
        val files = pl.sg.build.FilesToConvertingToUnixFilter(listOf("yml", "Dockerfile", "sql", "sh", "json", "conf"))
                .textFiles(destination)
        val convertToUnixLineEndings = pl.sg.build.ConvertToUnixLineEndings()
        files.forEach { file -> convertToUnixLineEndings.forFile(file) }
        convertToUnixLineEndings.convert();
    }

    from(jar.archiveFile) {
        rename { "backend/accountant.jar" }
    }
    from(appProject.resolve("ovh").resolve("application.yml")) {
        rename { "backend/application.yml" }
    }
    from(clientProject.resolve("angular.json")) {
        into("client")
    }
    from(clientProject.resolve("nginx.conf")) {
        into("client")
    }
    from(clientProject.resolve("mime.types")) {
        into("client")
    }
    from(clientProject.resolve("package.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.app.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.base.json")) {
        into("client")
    }
    from(clientProject.resolve("tsconfig.json")) {
        into("client")
    }
    from(clientProject.resolve("src")) {
        into("client/src")
    }
    from(appProject.resolve("ovh")) {
        exclude("application.yml")
    }
    destinationDir = destination.toFile()
}

apply(from = "$rootDir/integrationTest.gradle.kts")