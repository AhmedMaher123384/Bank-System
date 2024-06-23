package com.App.BankingSystem.Exception;

import org.springframework.http.HttpStatus;

public class CardNumberNotFoundException extends ApiBaseException {

    public CardNumberNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
