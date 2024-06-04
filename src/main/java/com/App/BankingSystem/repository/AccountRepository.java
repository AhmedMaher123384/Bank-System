package com.App.BankingSystem.repository;

import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCardNumber(String cardNumber);

    List<Account> findAllByUser(Users user);

    Optional<Account> findByCardNumber(String cardNumber);

    Optional<Account> findByCardNumberAndCvv(String cardNumber, String cvv);
}
