package pl.sg.build;

import kotlin.text.Charsets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConvertToUnixLineEndings {
    static final String CRLF = "\r\n";
    static final String LF = "\n";
    static final String CR = "\r";

    private final List<Path> filePaths = new ArrayList<>();

    public void convert() throws IOException {
        for (Path path : filePaths) {
            List<String> lines = Files.readAllLines(path, Charsets.UTF_8);
            String out = String.join(LF, lines);
            Files.writeString(path, out, Charsets.UTF_8);
        }
    }

    public ConvertToUnixLineEndings forFile(Path path) {
        this.filePaths.add(path);
        return this;
    }
}
