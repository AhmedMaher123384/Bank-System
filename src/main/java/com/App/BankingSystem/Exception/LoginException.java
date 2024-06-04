package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class LoginException extends ApiBaseException {

    public LoginException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
