package com.molniya.molniya_backend.enums;

import lombok.Getter;

@Getter
public enum FileGroup {
    USERPHOTO("users"),
    TRAININGPHOTO("trainings"),
    PROFUCTPHOTO("products"),
    OTHER("other");

    private final String folderName;

    FileGroup(String folderName) {
        this.folderName = folderName;
    }
}
