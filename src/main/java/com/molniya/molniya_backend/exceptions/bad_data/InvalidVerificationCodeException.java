package com.molniya.molniya_backend.exceptions.bad_data;

import com.molniya.molniya_backend.exceptions.ApiException;
import org.springframework.http.HttpStatus;

/**
 * Ошибка, когда верификационный код неверный.
 */
public class InvalidVerificationCodeException extends ApiException {
    public InvalidVerificationCodeException() {
        super("Неверный код верификации", HttpStatus.BAD_REQUEST);
    }
}
