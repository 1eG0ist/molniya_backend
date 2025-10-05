package com.molniya.molniya_backend.exceptions.no_data;

import com.molniya.molniya_backend.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundDataException extends ApiException {
    public NotFoundDataException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
