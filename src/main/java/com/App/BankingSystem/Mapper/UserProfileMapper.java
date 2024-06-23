package com.App.BankingSystem.Mapper;

import com.App.BankingSystem.model.Dto.Response.UserProfileResponse;
import com.App.BankingSystem.model.entity.Users;

public interface UserProfileMapper {
    UserProfileResponse toUserProfile(Users user);
}
