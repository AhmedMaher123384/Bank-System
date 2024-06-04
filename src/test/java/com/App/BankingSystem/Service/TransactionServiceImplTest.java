package com.App.BankingSystem.Service;

import com.App.BankingSystem.Exception.LowBalanceException;
import com.App.BankingSystem.Mapper.TransactionMapper;
import com.App.BankingSystem.Service.Impl.TransactionServiceImpl;
import com.App.BankingSystem.model.Dto.DepositRequest;import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.Dto.WithdrawRequest;import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Transaction;import com.App.BankingSystem.model.entity.TransactionType;
import com.App.BankingSystem.repository.AccountRepository;import com.App.BankingSystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.InjectMocks;
import org.mockito.Mock;import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
 public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account testAccount;

    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        testAccount = new Account();
        testAccount.setCardNumber("1234567890123456");
        testAccount.setCvv("123");
        testAccount.setBalance(1000.0);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(100.0);
        testTransaction.setAccount(testAccount);
        testTransaction.setType(TransactionType.DEPOSIT);
    }
    @Test
    public void testDeposit() {
        DepositRequest request = new DepositRequest();
        request.setCard_number("1234567890123456");
        request.setAmount(100.0);

        when(accountRepository.findByCardNumber("1234567890123456")).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toEntity(eq(100.0), eq(testAccount), eq(TransactionType.DEPOSIT))).thenReturn(testTransaction);
        when(transactionMapper.toResponseModel(eq(1L), eq(100.0), eq(1100.0))).thenReturn(new TransactionResponse(1L, 100.0, 1100.0));

        TransactionResponse response = transactionService.deposit(request);

        assertEquals(1L, response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals(1100.0, response.getBalance());

        verify(accountRepository).save(testAccount);
    }

    @Test
    public void testWithdraw() {
        WithdrawRequest request = new WithdrawRequest();
        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(100.0);
        when(accountRepository.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toEntity(eq(100.0), eq(testAccount), eq(TransactionType.WITHDRAW))).thenReturn(testTransaction);
        when(transactionMapper.toResponseModel(eq(1L), eq(100.0), eq(900.0))).thenReturn(new TransactionResponse(1L, 100.0, 900.0));

        TransactionResponse response = transactionService.withdraw(request);

        assertEquals(1L, response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals(900.0, response.getBalance());

        verify(accountRepository).save(testAccount);
    }
    @Test    void testWithdrawLowBalance() {
        WithdrawRequest request = new WithdrawRequest();

        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(2000.0);

        when(accountRepository.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(Optional.of(testAccount));

        assertThrows(LowBalanceException.class, () -> transactionService.withdraw(request));

    }

    @Test
    public void testBadCredentialsDeposit() {
        DepositRequest request = new DepositRequest();
        request.setCard_number("1234567890123456");
        request.setAmount(100.0);

        when(accountRepository.findByCardNumber("1234567890123456")).thenReturn(Optional.empty());
        assertThrows(BadCredentialsException.class, () -> transactionService.deposit(request));

    }

    @Test
    public void testBadCredentialsWithdraw() {
        WithdrawRequest request = new WithdrawRequest();

        request.setCard_number("1234567890123456");
        request.setCvv("123");
        request.setAmount(100.0);

        when(accountRepository.findByCardNumberAndCvv("1234567890123456", "123")).thenReturn(Optional.empty());
        assertThrows(BadCredentialsException.class, () -> transactionService.withdraw(request));

    }
}