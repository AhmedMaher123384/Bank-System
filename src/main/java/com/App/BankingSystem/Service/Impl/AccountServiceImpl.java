package com.App.BankingSystem.Service.Impl;

import com.App.BankingSystem.Mapper.AccountMapper;
import com.App.BankingSystem.Service.AccountService;
import com.App.BankingSystem.SpringUtils.Utils;
import com.App.BankingSystem.model.Dto.AccountResponse;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.AccountRepository;
import com.App.BankingSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountServiceImpl  implements AccountService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private  AccountMapper accountMapper;

    @Override
    public AccountResponse createNewAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " Not Found"));

        Account account = accountRepository.save(
                Account
                        .builder()
                        .cardNumber(generateUniqueCardNumber())
                        .cvv(Utils.generateCVV())
                        .balance(0.0)
                        .user(user)
                        .build()
        );

        return accountMapper.toResponseModel(account);
    }

    @Override
    public List<AccountResponse> getMyAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " Not Found"));

        return accountRepository
                .findAllByUser(user)
                .stream()
                .map(accountMapper::toResponseModel)
                .toList();
    }


    public String generateUniqueCardNumber() {
        String cardNumber = Utils.generateCardNumber();

        while (accountRepository.existsByCardNumber(cardNumber)) {
            cardNumber = Utils.generateCardNumber();
        }

        return cardNumber;
    }

}
