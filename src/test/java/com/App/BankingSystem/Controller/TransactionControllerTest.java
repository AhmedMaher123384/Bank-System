package com.App.BankingSystem.Controller;

import com.App.BankingSystem.Service.Impl.TransactionServiceImpl;
import com.App.BankingSystem.model.Dto.DepositRequest;
import com.App.BankingSystem.model.Dto.TransactionResponse;
import com.App.BankingSystem.model.Dto.WithdrawRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionServiceImpl transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DepositRequest depositRequest;
    private WithdrawRequest withdrawRequest;
    private TransactionResponse transactionResponse;

    @BeforeEach
    public void init(){
        depositRequest = DepositRequest.builder()
                .card_number("1")
                .amount(100.0)
                .build();

        withdrawRequest = WithdrawRequest.builder()
                .card_number("1")
                .cvv("123")
                .amount(50.0)
                .build();

        transactionResponse = TransactionResponse.builder()
                .id(1L)
                .amount(100.0)
                .balance(1000.0)
                .build();
    }

    @Test
    public void TransactionController_Deposit_ReturnsTransactionResponse() throws Exception {
        when(transactionService.deposit(depositRequest)).thenReturn(transactionResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(transactionResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", CoreMatchers.is(transactionResponse.getAmount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(transactionResponse.getBalance())));
    }

    @Test
    public void TransactionController_Withdraw_ReturnsTransactionResponse() throws Exception {
        transactionResponse.setAmount(50.0);
        when(transactionService.withdraw(withdrawRequest)).thenReturn(transactionResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(transactionResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", CoreMatchers.is(transactionResponse.getAmount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(transactionResponse.getBalance())));
    }
}