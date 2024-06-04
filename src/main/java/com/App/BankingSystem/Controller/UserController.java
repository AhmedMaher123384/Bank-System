package com.App.BankingSystem.Controller;

import com.App.BankingSystem.Service.Impl.UserServiceImpl;
import com.App.BankingSystem.model.Dto.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        return new ResponseEntity<>(userService.getUserProfile(),HttpStatus.OK);

    }
}
