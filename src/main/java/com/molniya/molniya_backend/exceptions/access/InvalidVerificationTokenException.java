package com.molniya.molniya_backend.exceptions.access;

import com.molniya.molniya_backend.exceptions.ApiException;
import org.springframework.http.HttpStatus;

/**
 * Ошибка, когда верификационный токен не совпадает с тем, что был выдан устройству.
 */
public class InvalidVerificationTokenException extends ApiException {
    public InvalidVerificationTokenException() {
        super("Неверный токен запроса. Код необходимо ввести с того же устройства, на котором он запрашивался.", HttpStatus.UNAUTHORIZED);
    }
}