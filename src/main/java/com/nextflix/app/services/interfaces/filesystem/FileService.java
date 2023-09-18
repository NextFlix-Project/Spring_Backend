package com.nextflix.app.services.interfaces.filesystem;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public void saveFile(MultipartFile file, String path) throws IOException;
    public byte[] getFile(String file) throws IOException;
}
