package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.Security.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<String> register(@RequestBody RegisterDto registerDto);
    ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto);
    ResponseEntity<AccessTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request);
    ResponseEntity<String> changePassword(ChangePasswordRequest request, Authentication authentication);

    }
