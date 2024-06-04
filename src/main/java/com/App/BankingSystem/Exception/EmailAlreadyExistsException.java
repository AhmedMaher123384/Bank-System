package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiBaseException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}