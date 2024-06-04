package com.App.BankingSystem.Service;

import com.App.BankingSystem.model.Dto.Security.LogoutRequest;
import org.springframework.http.ResponseEntity;

public interface LogoutService {
    ResponseEntity<String> logout(LogoutRequest logoutRequestDto) ;

}
