package org.example.utils;

import org.example.App;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Resources {
    public static final String ABSOLUTE_PATH = "src/main/resources/";

    private static Path getResourcePath(String path) {
        return Paths.get(
                Objects.requireNonNull(App.class.getClassLoader().getResource(path)).getPath()
        );
    }

    public static List<String> readLinesFrom(String path) {
        try {
            return Files.readAllLines(getResourcePath(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
