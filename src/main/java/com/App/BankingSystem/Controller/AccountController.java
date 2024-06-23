package com.App.BankingSystem.Controller;

import com.App.BankingSystem.Service.Impl.AccountServiceImpl;
import com.App.BankingSystem.model.Dto.Response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")

public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping("/add/")
    public ResponseEntity<AccountResponse> createNewAccount() {
        return new ResponseEntity<>(accountService.createNewAccount(),HttpStatus.CREATED);

    }

    @GetMapping("/get/")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        return new ResponseEntity<>(accountService.getMyAccounts(),HttpStatus.OK);
    }

    @GetMapping("/balance/{cardNumber}")
    public ResponseEntity<Double> getBalance(@PathVariable String cardNumber) {
        double balance = accountService.getBalance(cardNumber);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}

