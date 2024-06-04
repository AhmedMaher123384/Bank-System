package com.App.BankingSystem.Controller;


import com.App.BankingSystem.Service.Impl.AuthServiceImpl;
import com.App.BankingSystem.Service.Impl.LogoutServiceImpl;
import com.App.BankingSystem.model.Dto.Security.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private LogoutServiceImpl logoutService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterDto registerDto;
    private LoginDto loginDto;
    private RefreshTokenRequest refreshTokenRequest;
    private ChangePasswordRequest changePasswordRequest;
    private LogoutRequest logoutRequest;
    private TokenResponse tokenResponse;
    private AccessTokenResponse accessTokenResponse;
    private Authentication authentication;

    @BeforeEach
    public void init() {
        registerDto = new RegisterDto("name","email", "password","0101");
        loginDto = new LoginDto("email", "password");
        refreshTokenRequest = new RefreshTokenRequest("validRefreshToken");
        changePasswordRequest = new ChangePasswordRequest("currentPassword", "newPassword", "newPassword");
        logoutRequest = new LogoutRequest("validAccessToken", "validRefreshToken");
        tokenResponse = new TokenResponse("accessToken");
        tokenResponse.setRefreshToken("refreshToken");
        accessTokenResponse = new AccessTokenResponse("newAccessToken");
        UserDetails userDetails = User.withUsername("email").password("password").roles("USER").build();
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void AuthController_Register_ReturnCreated() throws Exception {
        when(authService.register(registerDto)).thenReturn(ResponseEntity.status(201).body("email registered successfully"));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("email registered successfully"));
    }

    @Test
    public void AuthController_Login_ReturnTokenResponse() throws Exception {
        when(authService.login(loginDto)).thenReturn(ResponseEntity.ok(tokenResponse));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", CoreMatchers.is(tokenResponse.getAccessToken())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken", CoreMatchers.is(tokenResponse.getRefreshToken())));
    }

    @Test
    public void AuthController_RefreshToken_ReturnAccessTokenResponse() throws Exception {
        when(authService.refreshToken(refreshTokenRequest)).thenReturn(ResponseEntity.ok(accessTokenResponse));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", CoreMatchers.is(accessTokenResponse.getAccessToken())));
    }

    @Test
    public void AuthController_ChangePassword_ReturnOk() throws Exception {
        when(authService.changePassword(any(ChangePasswordRequest.class), any(Authentication.class)))
                .thenReturn(ResponseEntity.ok("Password changed successfully"));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest))
                .principal(authentication));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password changed successfully"));
    }


    @Test
    public void AuthController_Logout_ReturnOk() throws Exception {
        when(logoutService.logout(logoutRequest)).thenReturn(ResponseEntity.ok("Logout Success"));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoutRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Logout Success"));
    }
}

