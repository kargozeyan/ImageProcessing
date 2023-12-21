package org.example.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resources {
    public static final String ABSOLUTE_PATH = "src/main/resources/";
    public static final String PROCESSED_IMAGE_PATH = "src/main/resources/ProcessedImages";

    private static Path getResourcePath(String path) {
        return Paths.get(
                Objects.requireNonNull(Resources.class.getClassLoader().getResource(path)).getPath()
        );
    }

    public static List<Path> getFileNames(String dir) {
        try (Stream<Path> paths = Files.walk(Paths.get(ABSOLUTE_PATH + dir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
