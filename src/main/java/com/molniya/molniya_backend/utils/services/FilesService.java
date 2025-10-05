package com.molniya.molniya_backend.utils.services;

import com.molniya.molniya_backend.enums.FileGroup;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    /**
     * Сохраняет файл и возвращает путь к нему
     */
    String saveFile(MultipartFile file, FileGroup type);

    /**
     * Удаляет файл по пути
     */
    boolean deleteFile(String filePath);

    /**
     * Проверяет существование файла
     */
    boolean fileExists(String filePath);

    /**
     * Получает расширение файла
     */
    String getFileExtension(String filename);

    /**
     * Генерирует уникальное имя файла
     */
    String generateUniqueFilename(String originalFilename);

    /**
     * Проверяет тип переданного файла
     */
    boolean isFileTypeSuitable(String[] allowedTypes, String fileName);
}
