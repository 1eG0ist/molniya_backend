package com.molniya.molniya_backend.exceptions.access;

import com.molniya.molniya_backend.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoAccessToOperationException extends ApiException {
    public NoAccessToOperationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
