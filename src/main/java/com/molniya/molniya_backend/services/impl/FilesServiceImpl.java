package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.enums.FileType;
import com.molniya.molniya_backend.services.FilesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesServiceImpl implements FilesService {

    private final String filesDir;
    private final String filesSep;

    public FilesServiceImpl(
            @Value("${file.windows.files-dir}") String windowsFilesDir,
            @Value("${file.windows.files-sep}") String windowsFilesSep,
            @Value("${file.linux.files-dir}") String linuxFilesDir,
            @Value("${file.linux.files-sep}") String linuxFilesSep) {

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            this.filesDir = windowsFilesDir;
            this.filesSep = windowsFilesSep;
        } else {
            this.filesDir = linuxFilesDir;
            this.filesSep = linuxFilesSep;
        }
    }

    @Override
    public String saveFile(MultipartFile file, FileType type) {
        return "";
    }

    @Override
    public boolean deleteFile(String filePath) {
        return false;
    }

    @Override
    public boolean fileExists(String filePath) {
        return false;
    }

    @Override
    public String getFileExtension(String filename) {
        return "";
    }

    @Override
    public String generateUniqueFilename(String originalFilename) {
        return "";
    }
}
