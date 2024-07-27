import nu.studer.gradle.jooq.JooqEdition
import org.apache.tools.ant.taskdefs.condition.Os
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import java.io.ByteArrayOutputStream

group = "pl.sg"
version = "0.0.1-SNAPSHOT"
val output = ByteArrayOutputStream()
var command = if (Os.isFamily(Os.FAMILY_MAC)) "dev-ops/setup/get-authorization-token.sh" else "aws codeartifact get-authorization-token --domain sg-repository --domain-owner 215372400964 --region eu-central-1 --query authorizationToken --output text"

project.exec {
    commandLine = command.split(" ")
            .filter { it.isNotBlank() }
    standardOutput = output
}
val codeartifactToken = output.toString()

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:8.7.1")
    }
}

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.5.1")
    }
}

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("jvm-test-suite")
    id("java")
    id("org.flywaydb.flyway") version "9.5.1"
    id("com.netflix.dgs.codegen") version "5.12.4"
    id("nu.studer.jooq") version "8.2.1"
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://sg-repository-215372400964.d.codeartifact.eu-central-1.amazonaws.com/maven/sg-repository/")
        credentials {
            username = "aws"
            password = codeartifactToken
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "pl.sg"
            artifactId = "sg-app"
            version = "1.0.0"

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://sg-repository-215372400964.d.codeartifact.eu-central-1.amazonaws.com/maven/sg-repository/")
            credentials {
                username = "aws"
                password = codeartifactToken
            }
        }
    }
}

dependencies {

    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")

    implementation("com.graphql-java:graphql-java:21.3")

    implementation("com.amazonaws:aws-java-sdk-s3:1.12.351")

    implementation("com.auth0:java-jwt:4.0.0")

    implementation("com.google.code.gson:gson:2.9.0")

    implementation("com.sendgrid:sendgrid-java:4.10.1")

    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")

    implementation("org.codehaus.groovy:groovy-all:3.0.13")
    implementation("org.codehaus.groovy:groovy:3.0.13")

    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")

    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    implementation("org.javamoney:moneta:1.4.4")

    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")

    implementation("org.jooq:jooq:3.19.1")

    implementation("org.jsoup:jsoup:1.15.3")

    implementation("org.modelmapper:modelmapper:3.1.0")
    implementation("org.modelmapper.extensions:modelmapper-jooq:3.2.0")

    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("pl.sg:pjm:1.0.2")
    implementation("pl.sg:loans:1.0.2")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok:1.18.24")

    runtimeOnly("com.github.joschi.jackson:jackson-datatype-threetenbp:2.12.5")

    runtimeOnly("org.postgresql:postgresql:42.5.1")

    annotationProcessor("org.projectlombok:lombok:1.18.30")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("com.jayway.jsonpath:json-path:2.7.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    testImplementation("org.hamcrest:hamcrest-library:2.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.4")
    testImplementation("org.springframework.boot:spring-boot-test")
    testImplementation("org.springframework:spring-test:5.3.23")

    testRuntimeOnly("org.springframework.security:spring-security-test:5.7.3")

//    integrationTestImplementation("net.logstash.logback:logstash-logback-encoder:7.2")
//    integrationTestImplementation("org.testcontainers:junit-jupiter:1.17.5")
//    integrationTestImplementation("org.testcontainers:postgresql:1.17.5")
//    integrationTestImplementation("org.testcontainers:testcontainers:1.17.5")
//    integrationTestImplementation("org.flywaydb:flyway-core:9.5.1")

    jooqGenerator("org.postgresql:postgresql:42.5.1")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    jooqGenerator(project(":sg-generator-strategy"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    exclude(
            "ovh",
            "application.yml",
            "application-pc-docker.yml",
            "application-raspberry-docker.yml",
            "data.sql"
    )
}

val migrate = tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrate") {
    url = System.getenv("SG_DB_URL") ?: "jdbc:postgresql://localhost:5432/accountant"
    user = "postgres"
    password = System.getenv("SG_DB_PASSWORD") ?: "SLAwek1!"

}

val dockerPackage = tasks.register<Zip>("dockerPackage") {
    from("docker/production/copy_to_docker/")
    include("*")
    archiveFileName.set("docker.zip")
}

val infrastructureRpi4 = tasks.register<Zip>("infrastructureRpi4") {
    from("docker/common/") {
        include("setup_directories.sh")
    }
    from("dev-ops/setup/infrastructure/") {
        include("cloud_watch_config.json")
        include("setup_files.sh")
        include("setup_docker.sh")
        include("setup.sh")
    }
    archiveFileName.set("infrastructureRpi4.zip")
}

val dockerPackageRpi4 = tasks.register<Zip>("dockerPackageRpi4") {
    from("dev-ops/apps/common") {
        include("management/*")
        include("setup/*")
        into("core")
    }
    from("dev-ops/apps/core") {
        include("management/*")
        include("setup/*")
        include("stack/*")
        include("stack/config/*")
        exclude("stack/README.md")
        into("core")
    }
    from("dev-ops/apps/common") {
        include("management/*")
        include("setup/*")
        into("db")
    }
    from("dev-ops/apps/db") {
        include("management/*")
        include("setup/*")
        include("stack/*")
        into("db")
    }
    from("dev-ops/apps/common") {
        include("management/*")
        include("setup/*")
        into("sg-application")
    }
    from("dev-ops/apps/sg-application") {
        include("management/*")
        include("setup/*")
        include("stack/*")
        include("stack/config/*")
        exclude("Dockerfile*")
        into("sg-application")
    }
    archiveFileName.set("dockerRpi4.zip")
}

tasks.named("build") {
    dependsOn(dockerPackage)
    dependsOn(dockerPackageRpi4)
    dependsOn(infrastructureRpi4)
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    generateClient = true
    packageName = "pl.sg.graphql.schema"
    typeMapping = mutableMapOf(
            "BigDecimal" to "java.math.BigDecimal",
            "UUID" to "java.util.UUID"
    )
}


jooq() {
    version.set("3.19.1")
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = System.getenv("SG_DB_URL") ?: "jdbc:postgresql://localhost:5432/accountant"
                    user = "postgres"
                    password = System.getenv("SG_DB_PASSWORD") ?: "SLAwek1!"
                    properties = listOf(
                            Property().apply {
                                key = "PAGE_SIZE"
                                value = "2048"
                            }
                    )
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    strategy.name = "pl.sg.SGGeneratorStrategy"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"

                        excludes = "PG_CATALOG.* | INFORMATION_SCHEMA.*"

                        forcedTypes = listOf(
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                }
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = false
                        isImmutablePojos = false
                        isFluentSetters = false
                    }
                    target.apply {
                        packageName = "pl.sg.jooq"
                        directory = "build/generated/sources/jooq"
                    }
                }
            }
        }
    }
}