package pl.sg.build


import java.io.File

data class ProcessResult(val output: String, val error: String, val exitCode: Int)

fun String.runCommand(workingDir: File): ProcessResult {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

    proc.waitFor()
    return ProcessResult(
            proc.inputStream.bufferedReader().readText(),
            proc.errorStream.bufferedReader().readText(),
            proc.exitValue()
    )
}
