package com.App.BankingSystem.Mapper.Impl;

import com.App.BankingSystem.Mapper.TransactionMapper;
import com.App.BankingSystem.model.Dto.Response.TransactionResponse;
import com.App.BankingSystem.model.Dto.Response.TransferResponse;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import com.App.BankingSystem.model.entity.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toEntity(double amount, Account account, TransactionType type) {
        return Transaction
                .builder()
                .amount(amount)
                .account(account)
                .type(type)
                .timestamp(new Date())
                .notes("Account Balance: " + account.getBalance())
                .build();
    }

    @Override
    public TransactionResponse toResponseModel(Long id, double amount, double balance) {
        return TransactionResponse
                .builder()
                .id(id)
                .amount(amount)
                .balance(balance)
                .build();
    }

    @Override
    public TransactionResponse toResponseModel(Transaction transaction) {
        return TransactionResponse
                .builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .balance(transaction.getAccount().getBalance())
                .type(transaction.getType())
                .timestamp(transaction.getTimestamp())
                .notes(transaction.getNotes())
                .build();
    }

    public TransferResponse toTransferResponse(Transaction transaction, String status, String message) {
        return TransferResponse
                .builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .balance(transaction.getAccount().getBalance())
                .status(status)
                .timestamp(transaction.getTimestamp())
                .message(message)
                .build();
    }
}
