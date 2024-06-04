package com.App.BankingSystem.model.Dto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String accessToken ;
    private String refreshToken ;
    private String tokenType="Bearer token" ;

    public TokenResponse( String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponse(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
