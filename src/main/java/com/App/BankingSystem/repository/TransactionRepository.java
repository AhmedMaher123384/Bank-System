package com.App.BankingSystem.repository;

import com.App.BankingSystem.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

