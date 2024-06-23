package com.App.BankingSystem.Controller;


import com.App.BankingSystem.Service.Impl.TransactionServiceImpl;
import com.App.BankingSystem.model.Dto.Request.DepositRequest;
import com.App.BankingSystem.model.Dto.Request.TransferRequest;
import com.App.BankingSystem.model.Dto.Request.WithdrawRequest;
import com.App.BankingSystem.model.Dto.Response.TransactionResponse;
import com.App.BankingSystem.model.Dto.Response.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {


    private final TransactionServiceImpl transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody WithdrawRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transfer(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history/{cardNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String cardNumber) {
        List<TransactionResponse> history = transactionService.getTransactionHistory(cardNumber);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}