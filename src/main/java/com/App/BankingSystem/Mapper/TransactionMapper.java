package com.App.BankingSystem.Mapper;


import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import com.App.BankingSystem.model.entity.TransactionType;

public interface TransactionMapper {
    Transaction toEntity(double amount, Account account, TransactionType type);
    TransactionResponse toResponseModel(Long id, double amount, double balance);
}
