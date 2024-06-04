package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ApiBaseException {

    public RoleNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
