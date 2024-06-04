package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class TokenValidationException extends ApiBaseException {

    public TokenValidationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}