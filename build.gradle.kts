buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.5.0")
    }
}

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("jvm-test-suite")
    id("java")
    id("org.flywaydb.flyway") version "9.5.1"
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

dependencies {
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.345")
    implementation("com.auth0:java-jwt:4.0.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.sendgrid:sendgrid-java:4.9.3")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.codehaus.groovy:groovy-all:3.0.13")
    implementation("org.codehaus.groovy:groovy:3.0.13")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.modelmapper:modelmapper:3.1.0")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.github.joschi.jackson:jackson-datatype-threetenbp:2.12.5")
    runtimeOnly("org.postgresql:postgresql:42.5.0")

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