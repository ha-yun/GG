package com.example.msastarboard.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private final Path root = Paths.get("uploads");

    // 파일 업로드 처리
    public String uploadFile(MultipartFile file) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.root.resolve(filename));

        return "/uploads/" + filename;
    }
}
