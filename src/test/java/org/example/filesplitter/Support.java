package org.example.filesplitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Support {

    private final Path path;

    public Support(Path path) {
        this.path = path;
    }

    public Path createFile(String name) {
        try {
            return Files.createFile(path.resolve(name));
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "Could not create file with name %s in path %s", name, path), e);
        }
    }

}
