package com.App.BankingSystem.repository;

import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
}

