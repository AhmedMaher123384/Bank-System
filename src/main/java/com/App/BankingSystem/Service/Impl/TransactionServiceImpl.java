package com.App.BankingSystem.Service.Impl;

import com.App.BankingSystem.Exception.LowBalanceException;
import com.App.BankingSystem.Mapper.TransactionMapper;
import com.App.BankingSystem.Service.TransactionService;
import com.App.BankingSystem.model.Dto.Request.DepositRequest;
import com.App.BankingSystem.model.Dto.Request.TransferRequest;
import com.App.BankingSystem.model.Dto.Request.WithdrawRequest;
import com.App.BankingSystem.model.Dto.Response.TransactionResponse;
import com.App.BankingSystem.model.Dto.Response.TransferResponse;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import com.App.BankingSystem.model.entity.TransactionType;
import com.App.BankingSystem.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountServiceImpl accountService;

    @Transactional
    @Override
    public TransactionResponse deposit(DepositRequest request) {
        Account account = accountService.findByCardNumber(request.getCard_number());
        Transaction transaction = performDeposit(account, request.getAmount());
        return transactionMapper.toResponseModel(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = accountService.findByCardNumberAndCvv(request.getCard_number(), request.getCvv());
        Transaction transaction = performWithdrawal(account, request.getAmount());
        return transactionMapper.toResponseModel(transaction);
    }

    @Transactional
    @Override
    public TransferResponse transfer(TransferRequest request) {
        Account sourceAccount = accountService.findByCardNumberAndCvv(request.getSourceCardNumber(), request.getCvv());
        Account destinationAccount = accountService.findByCardNumber(request.getDestinationCardNumber());

        if (sourceAccount.getBalance() < request.getAmount()) {
            throw new LowBalanceException("Your Balance " + sourceAccount.getBalance() + "   NOT Enough " + request.getAmount());
        }

        performTransfer(sourceAccount, destinationAccount, request.getAmount());

        Transaction transaction = transactionMapper.toEntity(request.getAmount(), sourceAccount, TransactionType.TRANSFER_OUT);
        transactionRepository.save(transaction);

        return transactionMapper.toTransferResponse(transaction, "SUCCESS", "Transfer completed successfully.");
    }


    @Override
    public List<TransactionResponse> getTransactionHistory(String cardNumber) {
        Account account = accountService.findByCardNumber(cardNumber);
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return transactions.stream()
                .map(transactionMapper::toResponseModel)
                .collect(Collectors.toList());
    }


    private Transaction performDeposit(Account account, double amount) {
        accountService.updateAccountBalance(account, amount);
        return transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.DEPOSIT));
    }

    private Transaction performWithdrawal(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new LowBalanceException("Your Balance " + account.getBalance() + "   NOT Enough " + amount);
        }

        accountService.updateAccountBalance(account, -amount);
        return transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.WITHDRAW));
    }


    private void performTransfer(Account sourceAccount, Account destinationAccount, double amount) {
        accountService.updateAccountBalance(sourceAccount, -amount);
        accountService.updateAccountBalance(destinationAccount, amount);

        Transaction sourceTransaction = transactionMapper.toEntity(amount, sourceAccount, TransactionType.TRANSFER_OUT);
        Transaction destinationTransaction = transactionMapper.toEntity(amount, destinationAccount, TransactionType.TRANSFER_IN);

        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);
    }
}
