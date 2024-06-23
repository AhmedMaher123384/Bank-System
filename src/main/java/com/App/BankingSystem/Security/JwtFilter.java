package com.App.BankingSystem.Security;


import com.App.BankingSystem.repository.AccessTokenBlackListRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;
    @Autowired
    private AccessTokenBlackListRepository accessTokenBlackListRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        final String header =  request.getHeader(SecurityConstants.TOKEN_HEADER);
        final SecurityContext securityContext= SecurityContextHolder.getContext();

        if(header != null && securityContext.getAuthentication() == null){
            String token = header.substring("Bearer ".length());
            String username = jwtUtil.extractUserName(token, SecurityConstants.SECRET_KEY);

            if(accessTokenBlackListRepository.findByToken(token).isPresent()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }

            if(username != null){
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if(jwtUtil.isAccessTokenValidate(token, user)){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
