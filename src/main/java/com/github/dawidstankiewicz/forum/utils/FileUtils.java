package com.github.dawidstankiewicz.forum.utils;

import java.util.Optional;

public class FileUtils {

    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .or(() -> Optional.of(""));
    }
}
