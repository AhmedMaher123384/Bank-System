package com.App.BankingSystem.Service.Impl;


import com.App.BankingSystem.Exception.TokenValidationException;
import com.App.BankingSystem.Security.CustomeUserDetailsService;
import com.App.BankingSystem.Security.JwtUtil;
import com.App.BankingSystem.SpringUtils.SecurityConstants;
import com.App.BankingSystem.Service.LogoutService;
import com.App.BankingSystem.model.Dto.Security.LogoutRequest;
import com.App.BankingSystem.model.entity.AccessTokenBlackList;
import com.App.BankingSystem.model.entity.RefreshTokenBlackList;
import com.App.BankingSystem.repository.AccessTokenBlackListRepository;
import com.App.BankingSystem.repository.RefreshTokenBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;
    @Autowired
    private AccessTokenBlackListRepository accessTokenBlackListRepository;
    @Autowired
    private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    @Override
    public ResponseEntity<String> logout(LogoutRequest logoutRequest) {
        if (!jwtUtil.isTokenValidate(logoutRequest.getAccessToken(), userDetailsService.loadUserByUsername(jwtUtil.extractUserName(logoutRequest.getAccessToken(), SecurityConstants.SECRET_KEY)), SecurityConstants.SECRET_KEY) ||
                !jwtUtil.isTokenValidate(logoutRequest.getRefreshToken(), userDetailsService.loadUserByUsername(jwtUtil.extractUserName(logoutRequest.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY)), SecurityConstants.REFRESH_SECRET_KEY)) {
            throw new TokenValidationException("Invalid token");
        }

        Date accessTokenExpiry = jwtUtil.extractExpirationDate(logoutRequest.getAccessToken(), SecurityConstants.SECRET_KEY);
        Date refreshTokenExpiry = jwtUtil.extractExpirationDate(logoutRequest.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY);

        AccessTokenBlackList blacklistedAccessToken = new AccessTokenBlackList(null, logoutRequest.getAccessToken(), accessTokenExpiry);
        RefreshTokenBlackList blacklistedRefreshToken = new RefreshTokenBlackList(null, logoutRequest.getRefreshToken(), refreshTokenExpiry);

        accessTokenBlackListRepository.save(blacklistedAccessToken);
        refreshTokenBlackListRepository.save(blacklistedRefreshToken);
        return new ResponseEntity<>("Logout Success", HttpStatus.OK);
    }
}
