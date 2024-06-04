package com.App.BankingSystem.Controller;

import com.App.BankingSystem.Service.Impl.AuthServiceImpl;
import com.App.BankingSystem.Service.Impl.LogoutServiceImpl;
import com.App.BankingSystem.model.Dto.Security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private LogoutServiceImpl logoutService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return this.authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto) {
        return this.authService.login(loginDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return this.authService.refreshToken(request);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request , Authentication authentication) {
        return this.authService.changePassword(request,authentication);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout( @RequestBody LogoutRequest logoutRequest) {
        return this.logoutService.logout(logoutRequest);
    }
}
