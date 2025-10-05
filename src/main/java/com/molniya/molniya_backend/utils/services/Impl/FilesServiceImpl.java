package com.molniya.molniya_backend.utils.services.Impl;

import com.molniya.molniya_backend.enums.FileGroup;
import com.molniya.molniya_backend.utils.services.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
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
    public String saveFile(MultipartFile file, FileGroup type) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFilename = generateUniqueFilename(fileExtension);

        String filePath = filesDir + type.getFolderName() + filesSep + uniqueFilename;

        try {
            saveFileToDisk(file, filePath);
            log.info("File saved successfully: {}", filePath);
            return filePath;
        } catch (IOException e) {
            log.error("Failed to save file: {}", filePath, e);
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("File deleted successfully: {}", filePath);
            } else {
                log.warn("File not found for deletion: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    @Override
    public String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public String generateUniqueFilename(String originalFilename) {
        String fileExtension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomId = UUID.randomUUID().toString().substring(0, 10);
        return timestamp + "_" + randomId + fileExtension;
    }

    @Override
    public boolean isFileTypeSuitable(String[] allowedTypes, String fileName) {
        if (allowedTypes == null || allowedTypes.length == 0) return false;

        String extension = getFileExtension(fileName);
        if (extension.isEmpty()) return false;

        for (String allowedType : allowedTypes) {
            if (allowedType.equals(extension)) return true;
        }

        return false;
    }

    /**
     * Вспомогательный метод для сохранения файла на диск
     */
    private void saveFileToDisk(MultipartFile file, String filePath) throws IOException {
        Path path = Paths.get(filePath);

        // Создаем директории если не существуют
        Files.createDirectories(path.getParent());

        // Сохраняем файл
        Files.write(path, file.getBytes());
    }
}
