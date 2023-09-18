package com.nextflix.app.services.implementations.filesystem;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nextflix.app.services.interfaces.filesystem.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public byte[] getFile(String file) throws IOException {
        Path filePath = Paths.get(file);

        return Files.readAllBytes(filePath);
    }

    @Override
    public void saveFile(MultipartFile file, String path) throws IOException {
        Path uploadPath = Paths.get(path);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = Paths.get(path, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
    }

}
