package com.example.eventManagement.dao;

import com.example.eventManagement.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByEmail(String email);
    Optional<Token> findByToken(String token);
    Token findTopByEmailOrderByIdDesc(String email);

    void deleteByEmail(String email);
}
