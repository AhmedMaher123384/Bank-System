package com.App.BankingSystem.model.Dto.Security;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 3,max = 32, message = "password size should be between 3 and 32 digit or character")
    private String password;

}