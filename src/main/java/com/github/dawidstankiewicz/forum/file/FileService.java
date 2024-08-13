package com.github.dawidstankiewicz.forum.file;


import com.github.dawidstankiewicz.forum.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private final String storageRootPath;

    public FileService(@Value("${forum.storage.rootPath}") String storageRootPath) {
        this.storageRootPath = storageRootPath;
    }

    public File saveFile(MultipartFile file, String subdirectory) {
        try {
            StringJoiner newFilename = new StringJoiner(".").add(UUID.randomUUID().toString()).add(FileUtils.getFileExtension(file.getOriginalFilename()).get());
            String filePath = storageRootPath + subdirectory;
            Files.createDirectories(Paths.get(filePath));
            File finalFile = new File(filePath + newFilename);
            Files.createFile(finalFile.toPath());
            log.info("Saving file to {}", finalFile.getAbsolutePath());
            file.transferTo(finalFile);
            return finalFile;
        } catch (
                IOException e) {
            log.error("Failed to save file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
