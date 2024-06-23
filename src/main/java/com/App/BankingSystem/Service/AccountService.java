package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.Response.AccountResponse;
import com.App.BankingSystem.model.entity.Account;

import java.util.List;

public interface AccountService {
    AccountResponse createNewAccount();
    List<AccountResponse> getMyAccounts();
    double getBalance(String cardNumber);
    Account findByCardNumberAndCvv(String cardNumber, String cvv);
    Account findByCardNumber(String cardNumber);
    void updateAccountBalance(Account account, double amount);
}