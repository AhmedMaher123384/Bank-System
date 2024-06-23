package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class WrongAccountException extends ApiBaseException {

    public WrongAccountException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
