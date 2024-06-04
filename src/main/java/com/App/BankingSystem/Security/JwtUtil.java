package com.App.BankingSystem.Security;


import com.App.BankingSystem.model.entity.AccessTokenBlackList;
import com.App.BankingSystem.model.entity.RefreshTokenBlackList;
import com.App.BankingSystem.repository.AccessTokenBlackListRepository;
import com.App.BankingSystem.repository.RefreshTokenBlackListRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static com.App.BankingSystem.SpringUtils.SecurityConstants.*;


@Component
public class JwtUtil {


    @Autowired
    private AccessTokenBlackListRepository accessTokenBlackListRepository;
    @Autowired
    private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    public String generateToken(UserDetails user) {
        return createToken(user, ACCESS_TOKEN_EXPIRATION, SECRET_KEY);
    }

    public String generateRefreshToken(UserDetails user) {
        return createToken(user, REFRESH_TOKEN_EXPIRATION, REFRESH_SECRET_KEY);
    }

    private String createToken(UserDetails user, Long expiration, byte[] key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("created", new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private Claims extractAllClaims(String token, byte[] key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public Date extractExpirationDate(String token, byte[] key) {
        return extractAllClaims(token, key).getExpiration();
    }

    public String extractUserName(String token, byte[] key) {
        return extractAllClaims(token, key).getSubject();
    }

    private Boolean isTokenExpired(String token, byte[] key) {
        return extractExpirationDate(token, key).before(new Date());
    }

    public Boolean isTokenValidate(String token, UserDetails user, byte[] key) {
        final String tokenName = extractUserName(token, key);
        String userName = user.getUsername();
        return (!isTokenExpired(token, key) && tokenName.equals(userName));
    }

    public Boolean isAccessTokenValidate(String token, UserDetails user) {
        Optional<AccessTokenBlackList> blacklistedToken = accessTokenBlackListRepository.findByToken(token);
        return blacklistedToken.isEmpty() && isTokenValidate(token, user, SECRET_KEY);
    }

    public Boolean isRefreshTokenValidate(String token, UserDetails user) {
        Optional<RefreshTokenBlackList> blacklistedToken = refreshTokenBlackListRepository.findByToken(token);
        return blacklistedToken.isEmpty() && isTokenValidate(token, user, REFRESH_SECRET_KEY);
    }
}


