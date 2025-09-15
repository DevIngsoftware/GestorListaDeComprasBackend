package com.example.trabajofinal.trabajofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trabajofinal.trabajofinal.models.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByUserEmail(String userEmail);
}