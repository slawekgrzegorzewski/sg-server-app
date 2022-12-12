import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.jooq.meta.jaxb.ForcedType
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.5.1")
    }
}

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("jvm-test-suite")
    id("java")
    id("org.flywaydb.flyway") version "9.5.1"
    id("com.netflix.dgs.codegen") version "5.6.0"
    id("nu.studer.jooq") version "8.0"
}

group = "pl.sg"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

testing {
    suites {
        configureEach {
            if (this is JvmTestSuite) {
                useJUnitJupiter()
            }
        }

        val integrationTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.INTEGRATION_TEST)
            sources {
                java {
                    setSrcDirs(listOf("src/integrationTest/java"))
                }
                resources {
                    setSrcDirs(listOf("src/integrationTest/resources"))
                }
            }
            dependencies {
                implementation(project)
                runtimeOnly(project)
            }
        }
    }
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

repositories {
    mavenCentral()
}

java.sourceSets["main"].java {
    srcDir("src/generated/java")
}

dependencies {

    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.graphql-java:graphql-java:19.2")

    implementation("com.amazonaws:aws-java-sdk-s3:1.12.351")
    implementation("com.auth0:java-jwt:4.0.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.sendgrid:sendgrid-java:4.9.3")

    implementation("commons-fileupload:commons-fileupload:1.4")

    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.codehaus.groovy:groovy-all:3.0.13")
    implementation("org.codehaus.groovy:groovy:3.0.13")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")
    implementation("org.jooq:jooq:3.17.6")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.modelmapper:modelmapper:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.modelmapper.extensions:modelmapper-jooq:3.1.0")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok:1.18.24")

    runtimeOnly("com.github.joschi.jackson:jackson-datatype-threetenbp:2.12.5")
    runtimeOnly("org.postgresql:postgresql:42.5.1")

    annotationProcessor("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("com.jayway.jsonpath:json-path:2.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.4")
    testImplementation("org.springframework.boot:spring-boot-test")
    testImplementation("org.springframework:spring-test:5.3.23")

    testRuntimeOnly("org.springframework.security:spring-security-test:5.7.3")

    integrationTestImplementation("net.logstash.logback:logstash-logback-encoder:7.2")
    integrationTestImplementation("org.testcontainers:junit-jupiter:1.17.5")
    integrationTestImplementation("org.testcontainers:postgresql:1.17.5")
    integrationTestImplementation("org.testcontainers:testcontainers:1.17.5")
    integrationTestImplementation("org.flywaydb:flyway-core:9.5.1")

    jooqGenerator("org.postgresql:postgresql:42.5.1")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    jooqGenerator(project(":sg-generator-strategy"))
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

val migrateLocal = tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateLocal") {
    url = "jdbc:postgresql://localhost:5432/accountant"
    user = "postgres"
    password = "SLAwek1!"
}

val migrateProduction = tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateProduction") {
    url = "jdbc:postgresql://grzegorzewski.org:5432/accountant"
    user = "postgres"
    password = ""
}

val dockerPackage = tasks.register<Zip>("dockerPackage") {
    from("docker/production/copy_to_docker/")
    include("*")
    archiveFileName.set("docker.zip")
}

tasks.named("build") {
    dependsOn(dockerPackage)
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
    version.set("3.17.6")
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://192.168.52.98:5432/accountant"
                    user = "postgres"
                    password = "SLAwek1!"
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
                        directory = "src/generated/java"
                    }
                }
            }
        }
    }
}