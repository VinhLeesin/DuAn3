package com.example.repo;

import com.example.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, String> {
    Optional<Account> findByUsernameAndPassword(String un, String pwd);
}
