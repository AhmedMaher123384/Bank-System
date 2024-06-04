package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.DepositRequest;
import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.Dto.WithdrawRequest;


public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawRequest request);
}