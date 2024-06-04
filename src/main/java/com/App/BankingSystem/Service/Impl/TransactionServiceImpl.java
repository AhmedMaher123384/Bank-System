package com.App.BankingSystem.Service.Impl;

import com.App.BankingSystem.Exception.LowBalanceException;
import com.App.BankingSystem.Mapper.TransactionMapper;
import com.App.BankingSystem.Service.TransactionService;
import com.App.BankingSystem.model.Dto.DepositRequest;
import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.Dto.WithdrawRequest;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import com.App.BankingSystem.model.entity.TransactionType;
import com.App.BankingSystem.repository.AccountRepository;
import com.App.BankingSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private  TransactionRepository transactionRepository;
    @Autowired
    private  TransactionMapper transactionMapper;

    @Override
    public TransactionResponse deposit(DepositRequest request) {
        Account account = accountRepository
                .findByCardNumber(request.getCard_number())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Long transactionId = performDeposit(account, request.getAmount());

        return transactionMapper.toResponseModel(transactionId, request.getAmount(), account.getBalance());
    }

    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = accountRepository
                .findByCardNumberAndCvv(request.getCard_number(), request.getCvv())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Long transactionId = performWithdrawal(account, request.getAmount());

        return transactionMapper.toResponseModel(transactionId, request.getAmount(), account.getBalance());
    }

    private Long performDeposit(Account account, double amount) {
        updateAccountBalance(account, amount);
        Transaction transaction = transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.DEPOSIT));
        return transaction.getId();
    }

    private Long performWithdrawal(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new LowBalanceException("Your Balance " + account.getBalance() + "   NOT Enough " + amount);
        }

        updateAccountBalance(account, -amount);
        Transaction transaction = transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.WITHDRAW));
        return transaction.getId();
    }

    private void updateAccountBalance(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
