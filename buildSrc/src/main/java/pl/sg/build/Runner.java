package pl.sg.build;

import java.io.*;
import java.util.stream.Collectors;

public class Runner {

    public static ProcessResult runCommand(String command, File workingDir) throws IOException, InterruptedException {
        System.out.println("Running command: " + command);
        String[] parts = command.split("\\s");
        final var proc = new ProcessBuilder(parts)
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        proc.waitFor();
        System.out.println("Finished.");
        return new ProcessResult(
                toString(proc.getInputStream()),
                toString(proc.getErrorStream()),
                proc.exitValue()
        );
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(in)
        ) {
            return br.lines().collect(Collectors.joining());
        }
    }
}
