package com.App.BankingSystem.Mapper.Impl;

import com.App.BankingSystem.Mapper.UserProfileMapper;
import com.App.BankingSystem.model.Dto.Response.UserProfileResponse;
import com.App.BankingSystem.model.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapperImpl implements UserProfileMapper {
    @Override
    public UserProfileResponse toUserProfile(Users user) {
        return UserProfileResponse
                .builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}
