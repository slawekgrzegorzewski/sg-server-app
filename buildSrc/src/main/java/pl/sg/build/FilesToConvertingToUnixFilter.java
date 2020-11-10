package pl.sg.build;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilesToConvertingToUnixFilter {
    private final List<String> extensions;

    public FilesToConvertingToUnixFilter(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<Path> textFiles(Path dir) {
        return getFiles(dir).stream()
                .filter(file -> extensions.contains(getFileExtension(file)))
                .collect(Collectors.toList());
    }

    private List<Path> getFiles(Path dir) {
        List<Path> result = new ArrayList<>();
        if (dir.toFile().isFile()) {
            result.add(dir);
        } else {
            for (File file : dir.toFile().listFiles()) {
                result.addAll(getFiles(file.toPath()));
            }
        }
        return result;
    }

    private String getFileExtension(Path file) {
        String fileName = file.getFileName().toString();
        if (fileName.contains(".")) {
            String[] split = fileName.split("\\.");
            return split[split.length - 1];
        } else {
            return fileName;
        }
    }
}
