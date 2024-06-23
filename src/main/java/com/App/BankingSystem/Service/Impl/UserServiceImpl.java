package com.App.BankingSystem.Service.Impl;

import com.App.BankingSystem.Mapper.UserProfileMapper;
import com.App.BankingSystem.Service.UserService;
import com.App.BankingSystem.model.Dto.Response.UserProfileResponse;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " Not Found"));

        return userProfileMapper.toUserProfile(user);
    }
}
