package com.example.trabajofinal.trabajofinal.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trabajofinal.trabajofinal.models.UserModel;
import com.example.trabajofinal.trabajofinal.services.UserService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserModel user) {
        userService.register(user);
        Map<String, String> response = Map.of("message", "Usuario registrado exitosamente.");
        return ResponseEntity.ok(response);
    }

}