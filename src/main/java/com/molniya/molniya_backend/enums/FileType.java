package com.molniya.molniya_backend.enums;

import lombok.Getter;

@Getter
public enum FileType {
    USERPHOTO("users"),
    TRAININGPHOTO("trainings"),
    PROFUCTPHOTO("products"),
    OTHER("other");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }
}
