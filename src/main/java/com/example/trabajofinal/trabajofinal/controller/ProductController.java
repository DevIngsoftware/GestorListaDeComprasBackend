package com.example.trabajofinal.trabajofinal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.trabajofinal.trabajofinal.config.CustomUserDetails;
import com.example.trabajofinal.trabajofinal.models.ProductModel;
import com.example.trabajofinal.trabajofinal.models.UserModel;
import com.example.trabajofinal.trabajofinal.repository.ProductRepository;
import com.example.trabajofinal.trabajofinal.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public List<ProductModel> all(@AuthenticationPrincipal CustomUserDetails user,String authHeader) {
        return productRepository.findByUserEmail(user.getUsername());
    }

    @PostMapping
    public ProductModel add(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ProductModel product) {
        UserModel userModel= userRepository.findByEmail(user.getUsername()).orElseThrow();
        product.setUser(userModel);
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public ProductModel update(@PathVariable Long id, @RequestBody ProductModel product) {
        ProductModel existing = productRepository.findById(id).orElseThrow();
        existing.setName(product.getName());
        existing.setQuantity(product.getQuantity());
        existing.setNote(product.getNote());
        existing.setAcquired(product.isAcquired());
        return productRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
