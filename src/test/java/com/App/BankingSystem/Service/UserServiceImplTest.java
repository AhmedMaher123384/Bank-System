package com.App.BankingSystem.Service;

import com.App.BankingSystem.Mapper.UserProfileMapper;
import com.App.BankingSystem.Service.Impl.UserServiceImpl;
import com.App.BankingSystem.model.Dto.UserProfileResponse;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Users user;
    private UserProfileResponse userProfileResponse;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .name("ahmed")
                .email("test@example.com")
                .phone("123")
                .build();

        userProfileResponse = UserProfileResponse.builder()
                .name("ahmed")
                .email("ahmed@example.com")
                .phone("123")
                .build();

         authentication = mock(Authentication.class);
         securityContext = mock(SecurityContext.class);
    }

    @Test
    public void getUserProfile_ReturnsUserProfileResponse() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.of(user));
        when(userProfileMapper.toUserProfile(user)).thenReturn(userProfileResponse);

        UserProfileResponse result = userService.getUserProfile();
        assertThat(result).isEqualTo(userProfileResponse);
    }

    @Test
    public void getUserProfile_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("ahmed@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("ahmed@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserProfile();
        });

        assertThat(exception.getMessage()).contains("User ahmed@example.com Not Found");
    }
}