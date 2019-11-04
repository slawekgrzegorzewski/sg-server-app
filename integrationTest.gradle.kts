fun configurationsResolver(vararg strings: String) = strings.map { configurations[it] }.toTypedArray()

val integrationTestImplementation by configurations.creating() {
    extendsFrom(*configurationsResolver("implementation", "testImplementation"))
}

val integrationTestCompileOnly by configurations.creating() {
    extendsFrom(*configurationsResolver("compileOnly"))
}
val integrationTestAnnotationProcessor by configurations.creating() {
    extendsFrom(*configurationsResolver("annotationProcessor"))
}
val integrationTestRuntime by configurations.creating() {
    extendsFrom(*configurationsResolver("runtime", "testRuntime"))
}

val sourceSetsFromBuildScript = the<SourceSetContainer>()

val integrationTest by sourceSetsFromBuildScript.creating {
    java.srcDir("src/integrationTest/java")
    resources.srcDir("src/integrationTest/resources")
}


dependencies {
    integrationTestImplementation(sourceSetsFromBuildScript["main"].output)
    integrationTestImplementation(sourceSetsFromBuildScript["test"].output)
}

tasks.create<Test>("runIntegrationTest") {

    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs the integration tests."

    maxHeapSize = "1024m"

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath

    binResultsDir = file("$buildDir/integration-test-results/binary/integration-test")

    reports {
        html.destination = file("$buildDir/reports/integration-test")
        junitXml.destination = file("$buildDir/integration-test-results")
    }

    mustRunAfter(tasks["test"])
}

val runIntegrationTest = tasks["runIntegrationTest"]

gradle.projectsEvaluated {
    val quickTasks = mutableListOf<Task>()

    gradle.rootProject.allprojects.forEach { project ->
        quickTasks.addAll(project.tasks.toList().filter { it.name == "test" })
        quickTasks.addAll(project.tasks.withType(FindBugs::class.java))
        quickTasks.addAll(project.tasks.withType(Pmd::class.java))
    }

    quickTasks.forEach { runIntegrationTest.mustRunAfter(it) }
}