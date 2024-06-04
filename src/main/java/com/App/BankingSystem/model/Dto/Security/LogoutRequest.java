package com.App.BankingSystem.model.Dto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {

    private String accessToken ;
    private String refreshToken ;

}
