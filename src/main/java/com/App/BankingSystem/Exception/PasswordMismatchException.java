package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ApiBaseException {

    public PasswordMismatchException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}