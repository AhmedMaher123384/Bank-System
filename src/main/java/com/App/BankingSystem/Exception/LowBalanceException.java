package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class LowBalanceException extends ApiBaseException{

    public LowBalanceException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
