package pl.sg.build;

import kotlin.text.Charsets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GenerateJwtToken {
    static final String CRLF = "\r\n";
    static final String LF = "\n";
    static final String CR = "\r";

    private final List<Path> filePaths = new ArrayList<>();

    public void convert() throws IOException {
        for (Path path : filePaths) {
            List<String> lines = Files.readAllLines(path, Charsets.UTF_8);
            List<String> outLines = lines.stream()
                    .map(line -> {
                        if (line.contains("JWT_TOKEN: JWT_TOKEN")) {
                            return line.replace("JWT_TOKEN: JWT_TOKEN", "JWT_TOKEN: \"" + randomString(128) + "\"");
                        }
                        return line;
                    }).collect(Collectors.toList());
            Files.write(path, outLines);
        }
    }

    private String randomString(int length) {
        return new Random().ints(65, 90 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public GenerateJwtToken forFile(Path path) {
        this.filePaths.add(path);
        return this;
    }
}
