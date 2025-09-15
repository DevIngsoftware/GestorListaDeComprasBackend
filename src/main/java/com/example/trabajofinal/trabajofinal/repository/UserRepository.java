package com.example.trabajofinal.trabajofinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trabajofinal.trabajofinal.models.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
}