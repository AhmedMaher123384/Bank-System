package com.App.BankingSystem.repository;

import com.App.BankingSystem.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
