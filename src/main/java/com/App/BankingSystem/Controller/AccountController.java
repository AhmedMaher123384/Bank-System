package com.App.BankingSystem.Controller;

import com.App.BankingSystem.Service.Impl.AccountServiceImpl;
import com.App.BankingSystem.model.Dto.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

