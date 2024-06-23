package com.App.BankingSystem.Service.Impl;


import com.App.BankingSystem.Exception.*;
import com.App.BankingSystem.Security.CustomeUserDetailsService;
import com.App.BankingSystem.Security.JwtUtil;
import com.App.BankingSystem.Security.SecurityConstants;
import com.App.BankingSystem.Service.AuthService;
import com.App.BankingSystem.model.Dto.Security.*;
import com.App.BankingSystem.model.entity.Role;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.RoleRepository;
import com.App.BankingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;


    @Override
    public ResponseEntity<String> register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistsException("email already exists");
        }
        //Role UserRole = Role.builder()
        //.name("USER").build();
        //Role role = roleRepository.save(UserRole);
        Role role = roleRepository.findByName("USER").orElse(null);
        if (role == null) {
            throw new RoleNotFoundException("Role 'USER' not found");
        }

        String password = passwordEncoder.encode(registerDto.getPassword());

        Users user = Users.builder()
                .email(registerDto.getEmail())
                .password(password)
                .name(registerDto.getName())
                .phone(registerDto.getPhone())
                .roles(Collections.singletonList(role))
                .build();
        userRepository.save(user);

        return new ResponseEntity<>("email registered successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginDto loginDto) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserDetails user = userDetailsService.loadUserByUsername(loginDto.getEmail());
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            TokenResponse response = new TokenResponse(accessToken);
            response.setRefreshToken(refreshToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new LoginException("Invalid username or password");
        } catch (Exception e) {
            throw new LoginException("An error occurred during login");
        }
    }




    @Override
    public ResponseEntity<AccessTokenResponse> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtUtil.extractUserName(refreshToken, SecurityConstants.REFRESH_SECRET_KEY);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.isRefreshTokenValidate(refreshToken, userDetails)) {
            String newAccessToken = jwtUtil.generateToken(userDetails);
            AccessTokenResponse tokenResponse = new AccessTokenResponse(newAccessToken);

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequest request, Authentication authentication) {

        UserDetails userDetails =(UserDetails) authentication.getPrincipal();

        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new PasswordMismatchException("User not found"));

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new PasswordMismatchException("Wrong password");
        }

        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new PasswordMismatchException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);


    }

    }

