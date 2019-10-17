package pl.sg.build.compose

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import pl.sg.build.runCommand
import java.nio.file.Paths

open class DockerCompose : DefaultTask() {

    var machineName = "default"
    private val userDirectory = Paths.get(System.getProperty("user.home"))

    @TaskAction
    fun compose() {
        val (output, error, exitCode) = "docker-machine ls -f \"{{.Active}}\" --filter name=$machineName"
                .runCommand(userDirectory.toFile())
        println("Machine exists = " + if (machineExists(output)) "YES" else "NO")
        println("Machine is runnig = " + if (machineRunning(output)) "YES" else "NO")
    }

    private fun machineExists(output: String) = output.length > 0

    private fun machineRunning(output: String) = when (output) {
        "*\n" -> true
        "-\n" -> false
        else -> throw RuntimeException("Not known answer: $output")
    }
}