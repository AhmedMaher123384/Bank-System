package com.App.BankingSystem.Service;

import com.App.BankingSystem.Exception.LowBalanceException;
import com.App.BankingSystem.Mapper.TransactionMapper;
import com.App.BankingSystem.Service.Impl.TransactionServiceImpl;
import com.App.BankingSystem.Service.Impl.AccountServiceImpl;
import com.App.BankingSystem.model.Dto.Request.DepositRequest;
import com.App.BankingSystem.model.Dto.Response.TransactionResponse;
import com.App.BankingSystem.model.Dto.Request.TransferRequest;
import com.App.BankingSystem.model.Dto.Response.TransferResponse;
import com.App.BankingSystem.model.Dto.Request.WithdrawRequest;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;
import com.App.BankingSystem.model.entity.TransactionType;
import com.App.BankingSystem.repository.AccountRepository;
import com.App.BankingSystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {


    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account testAccount;
    private Account destinationAccount;
    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        testAccount = new Account();
        testAccount.setCardNumber("1234567890123456");
        testAccount.setCvv("123");
        testAccount.setBalance(1000.0);

        destinationAccount = new Account();
        destinationAccount.setCardNumber("6543210987654321");
        destinationAccount.setCvv("321");
        destinationAccount.setBalance(500.0);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(100.0);
        testTransaction.setAccount(testAccount);
        testTransaction.setType(TransactionType.DEPOSIT);
        testTransaction.setTimestamp(new Date());
        testTransaction.setNotes("Account Balance: 1000.0");
    }

    @Test
    public void testDeposit() {
        DepositRequest request = new DepositRequest();
        request.setCard_number("1234567890123456");
        request.setAmount(100.0);

        when(accountService.findByCardNumber("1234567890123456")).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toEntity(eq(100.0), eq(testAccount), eq(TransactionType.DEPOSIT))).thenReturn(testTransaction);
        when(transactionMapper.toResponseModel(eq(testTransaction)))
                .thenReturn(new TransactionResponse(1L, 100.0, 1100.0, TransactionType.DEPOSIT, testTransaction.getTimestamp(), "Account Balance: 1100.0"));

        TransactionResponse response = transactionService.deposit(request);

        assertEquals(1L, response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals(1100.0, response.getBalance());

        verify(accountService).updateAccountBalance(testAccount, 100.0);
        verify(transactionRepository).save(testTransaction);
    }

    @Test
    public void testWithdraw() {
        WithdrawRequest request = new WithdrawRequest();
        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(100.0);

        when(accountService.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toEntity(eq(100.0), eq(testAccount), eq(TransactionType.WITHDRAW))).thenReturn(testTransaction);
        when(transactionMapper.toResponseModel(eq(testTransaction)))
                .thenReturn(new TransactionResponse(1L, 100.0, 900.0, TransactionType.WITHDRAW, testTransaction.getTimestamp(), "Account Balance: 900.0"));

        TransactionResponse response = transactionService.withdraw(request);

        assertEquals(1L, response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals(900.0, response.getBalance());

        verify(accountService).updateAccountBalance(testAccount, -100.0);
        verify(transactionRepository).save(testTransaction);
    }

    @Test
    void testWithdrawLowBalance() {
        WithdrawRequest request = new WithdrawRequest();
        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(2000.0);

        when(accountService.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(testAccount);

        assertThrows(LowBalanceException.class, () -> transactionService.withdraw(request));
    }

    @Test
    public void testTransfer() {
        TransferRequest request = new TransferRequest();
        request.setSourceCardNumber("1234567890123456");
        request.setDestinationCardNumber("6543210987654321");
        request.setCvv("123");
        request.setAmount(100.0);

        Transaction sourceTransaction = new Transaction();
        sourceTransaction.setId(2L);
        sourceTransaction.setAmount(100.0);
        sourceTransaction.setAccount(testAccount);
        sourceTransaction.setType(TransactionType.TRANSFER_OUT);
        sourceTransaction.setTimestamp(new Date());
        sourceTransaction.setNotes("Account Balance: 900.0");

        Transaction destinationTransaction = new Transaction();
        destinationTransaction.setId(3L);
        destinationTransaction.setAmount(100.0);
        destinationTransaction.setAccount(destinationAccount);
        destinationTransaction.setType(TransactionType.TRANSFER_IN);
        destinationTransaction.setTimestamp(new Date());
        destinationTransaction.setNotes("Account Balance: 600.0");

        when(accountService.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(testAccount);
        when(accountService.findByCardNumber("6543210987654321")).thenReturn(destinationAccount);
        when(transactionMapper.toEntity(eq(100.0), eq(testAccount), eq(TransactionType.TRANSFER_OUT))).thenReturn(sourceTransaction);
        when(transactionMapper.toEntity(eq(100.0), eq(destinationAccount), eq(TransactionType.TRANSFER_IN))).thenReturn(destinationTransaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sourceTransaction).thenReturn(destinationTransaction);
        when(transactionMapper.toTransferResponse(eq(sourceTransaction), eq("SUCCESS"), eq("Transfer completed successfully.")))
                .thenReturn(new TransferResponse(2L, 100.0, 900.0, "SUCCESS", sourceTransaction.getTimestamp(), "Transfer completed successfully."));

        TransferResponse response = transactionService.transfer(request);

        assertEquals(2L, response.getTransactionId());
        assertEquals(100.0, response.getAmount());
        assertEquals(900.0, response.getBalance());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Transfer completed successfully.", response.getMessage());

        verify(accountService).updateAccountBalance(testAccount, -100.0);
        verify(accountService).updateAccountBalance(destinationAccount, 100.0);

        // Capturing the arguments to verify the correct transactions
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(3)).save(transactionCaptor.capture());

        List<Transaction> savedTransactions = transactionCaptor.getAllValues();
        assertEquals(TransactionType.TRANSFER_OUT, savedTransactions.get(0).getType());
        assertEquals(TransactionType.TRANSFER_IN, savedTransactions.get(1).getType());
    }


    @Test
    public void testGetTransactionHistory() {
        when(accountService.findByCardNumber("1234567890123456")).thenReturn(testAccount);
        when(transactionRepository.findByAccount(testAccount)).thenReturn(Collections.singletonList(testTransaction));
        when(transactionMapper.toResponseModel(eq(testTransaction)))
                .thenReturn(new TransactionResponse(1L, 100.0, 1000.0, TransactionType.DEPOSIT, testTransaction.getTimestamp(), "Account Balance: 1000.0"));

        List<TransactionResponse> transactionHistory = transactionService.getTransactionHistory("1234567890123456");

        assertEquals(1, transactionHistory.size());
        assertEquals(1L, transactionHistory.get(0).getId());
        assertEquals(100.0, transactionHistory.get(0).getAmount());
        assertEquals(1000.0, transactionHistory.get(0).getBalance());
    }

    @Test
    public void testBadCredentialsDeposit() {
        DepositRequest request = new DepositRequest();
        request.setCard_number("1234567890123456");
        request.setAmount(100.0);

        when(accountService.findByCardNumber("1234567890123456")).thenThrow(new BadCredentialsException("Card number not found"));
        assertThrows(BadCredentialsException.class, () -> transactionService.deposit(request));
    }

    @Test
    public void testBadCredentialsWithdraw() {
        WithdrawRequest request = new WithdrawRequest();
        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(100.0);

        when(accountService.findByCardNumberAndCvv("1234567890123456", "123")).thenThrow(new BadCredentialsException("Card number and CVV do not match"));
        assertThrows(BadCredentialsException.class, () -> transactionService.withdraw(request));
    }
}
