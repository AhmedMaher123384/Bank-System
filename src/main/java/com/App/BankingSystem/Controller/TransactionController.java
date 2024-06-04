package com.App.BankingSystem.Controller;


import com.App.BankingSystem.Service.Impl.TransactionServiceImpl;
import com.App.BankingSystem.model.Dto.DepositRequest;
import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.Dto.WithdrawRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        return new ResponseEntity<>(transactionService.deposit(request),HttpStatus.OK);

    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return new ResponseEntity<>(transactionService.withdraw(request),HttpStatus.OK);
    }
}