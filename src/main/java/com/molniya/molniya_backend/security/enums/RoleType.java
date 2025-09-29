package com.molniya.molniya_backend.security.enums;

import lombok.Getter;

/// All roles, which is in db, this enum created for using in security area
@Getter
public enum RoleType {
    PLAYER("PLAYER", "ROLE_PLAYER"),
    ADMIN("ADMIN", "ROLE_ADMIN"),
    TRAINER("TRAINER", "ROLE_TRAINER");

    private final String name; // For annotations (@PreAuthorize)
    private final String fullName;  // For custom check and DB

    RoleType(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }
}
