package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.Request.DepositRequest;
import com.App.BankingSystem.model.Dto.Request.TransferRequest;
import com.App.BankingSystem.model.Dto.Request.WithdrawRequest;
import com.App.BankingSystem.model.Dto.Response.TransactionResponse;
import com.App.BankingSystem.model.Dto.Response.TransferResponse;

import java.util.List;


public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawRequest request);
    List<TransactionResponse> getTransactionHistory(String cardNumber);
    TransferResponse transfer(TransferRequest request);
}