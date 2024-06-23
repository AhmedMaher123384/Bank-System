package com.App.BankingSystem.Security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


public class SecurityConstants {

    public static final Long ACCESS_TOKEN_EXPIRATION =  604800000L;  //  7 days
    public static final Long REFRESH_TOKEN_EXPIRATION = 2592000000L; // 30 days
    public static final String TOKEN_HEADER = "Authorization";
    public static final byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
    public static final byte[] REFRESH_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();



}
