package com.App.BankingSystem.Controller;



import com.App.BankingSystem.Service.Impl.UserServiceImpl;
import com.App.BankingSystem.model.Dto.UserProfileResponse;
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

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserProfileResponse userProfileResponse;

    @BeforeEach
    public void init() {
        userProfileResponse = UserProfileResponse.builder()
                .name("ahmed")
                .email("test@example.com")
                .phone("123")
                .build();
    }

    @Test
    public void UserController_GetUserProfile_ReturnsUserProfileResponse() throws Exception {
        when(userService.getUserProfile()).thenReturn(userProfileResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(userProfileResponse.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userProfileResponse.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", CoreMatchers.is(userProfileResponse.getPhone())));

    }
}
