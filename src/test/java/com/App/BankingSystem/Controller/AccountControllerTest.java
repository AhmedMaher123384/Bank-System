package com.App.BankingSystem.Controller;
import com.App.BankingSystem.Service.Impl.AccountServiceImpl;
import com.App.BankingSystem.model.Dto.AccountResponse;
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
import java.util.Collections;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @MockBean
    private AccountServiceImpl accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountResponse accountResponse;

    @BeforeEach
    public void init() {
        accountResponse = AccountResponse.builder()
                .card_number("1234567890")
                .cvv("123")
                .balance(1000.0)
                .build();
    }

    @Test
    public void AccountController_CreateNewAccount_ReturnsAccountResponse() throws Exception {
        when(accountService.createNewAccount()).thenReturn(accountResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/add/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.card_number", CoreMatchers.is(accountResponse.getCard_number())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cvv", CoreMatchers.is(accountResponse.getCvv())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(accountResponse.getBalance())));
    }

    @Test
    public void AccountController_GetMyAccounts_ReturnsListOfAccountResponse() throws Exception {
        when(accountService.getMyAccounts()).thenReturn(Collections.singletonList(accountResponse));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/get/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].card_number", CoreMatchers.is(accountResponse.getCard_number())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cvv", CoreMatchers.is(accountResponse.getCvv())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", CoreMatchers.is(accountResponse.getBalance())));

    }

}