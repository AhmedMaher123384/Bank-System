package com.App.BankingSystem.Service;

import com.App.BankingSystem.Mapper.AccountMapper;
import com.App.BankingSystem.Service.Impl.AccountServiceImpl;
import com.App.BankingSystem.model.Dto.AccountResponse;
import com.App.BankingSystem.model.entity.Account;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.AccountRepository;
import com.App.BankingSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Users user;
    private Account account;
    private AccountResponse accountResponse;
    private Authentication authentication;
    private SecurityContext securityContext ;

    @BeforeEach
    public void setUp() {
        user = Users.builder().email("ahmed@example.com").build();
        account = Account.builder().cardNumber("1234567890").cvv("123").balance(0.0).user(user).build();
        accountResponse = AccountResponse.builder().card_number("1234567890").cvv("123").balance(0.0).build();
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
    }

    @Test
    public void createNewAccount_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toResponseModel(any(Account.class))).thenReturn(accountResponse);

        AccountResponse result = accountService.createNewAccount();

        Assertions.assertThat(result).isEqualTo(accountResponse);
    }

    @Test
    public void createNewAccount_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.createNewAccount())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User ahmed@example.com Not Found");
    }

    @Test
    public void getMyAccounts_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.findAllByUser(user)).thenReturn(Collections.singletonList(account));
        when(accountMapper.toResponseModel(account)).thenReturn(accountResponse);

        List<AccountResponse> result = accountService.getMyAccounts();

        Assertions.assertThat(result).containsExactly(accountResponse);
    }

    @Test
    public void getMyAccounts_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.getMyAccounts())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User ahmed@example.com Not Found");
    }

}