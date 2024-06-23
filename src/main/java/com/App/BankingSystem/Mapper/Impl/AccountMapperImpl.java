package com.App.BankingSystem.Mapper.Impl;

import com.App.BankingSystem.Mapper.AccountMapper;
import com.App.BankingSystem.model.Dto.Response.AccountResponse;
import com.App.BankingSystem.model.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {


    public AccountResponse toResponseModel(Account account) {
        return AccountResponse
                .builder()
                .card_number(account.getCardNumber())
                .cvv(account.getCvv())
                .balance(account.getBalance())
                .build();
    }
}