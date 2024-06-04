package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createNewAccount();

    List<AccountResponse> getMyAccounts();
}