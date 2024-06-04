package com.App.BankingSystem.Mapper;

import com.App.BankingSystem.model.Dto.AccountResponse;
import com.App.BankingSystem.model.entity.Account;

public interface AccountMapper {
    AccountResponse toResponseModel(Account account);

}
