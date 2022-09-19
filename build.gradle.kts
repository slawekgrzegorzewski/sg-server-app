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
    implementation("org.codehaus.groovy:groovy-all:3.0.8")

    implementation("org.modelmapper:modelmapper:2.4.4")
    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("com.sendgrid:sendgrid-java:4.7.0")

    implementation("org.apache.poi:poi:3.15")
    implementation("org.apache.poi:poi-ooxml:3.15")

    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly("org.springframework.boot:spring-boot-devtools:2.3.3.RELEASE")

    runtimeOnly("org.postgresql:postgresql:42.2.6")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.3.3.RELEASE")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    testImplementation("junit:junit:4.12")
    testImplementation("org.springframework:spring-test:4.3.2.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-test:2.3.3.RELEASE")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("com.jayway.jsonpath:json-path:2.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.1.RELEASE")
    testImplementation("io.rest-assured:rest-assured:4.3.0")

    testRuntimeOnly("org.springframework.security:spring-security-test:5.1.5.RELEASE")
}

tasks.withType<Jar> {
    exclude(
            "ovh",
            "application.yml",
            "application-pc-docker.yml",
            "application-raspberry-docker.yml",
            "data.sql")
}

apply(from = "$rootDir/integrationTest.gradle.kts")