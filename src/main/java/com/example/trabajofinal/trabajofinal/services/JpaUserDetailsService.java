package com.example.trabajofinal.trabajofinal.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trabajofinal.trabajofinal.config.CustomUserDetails;
import com.example.trabajofinal.trabajofinal.models.UserModel;
import com.example.trabajofinal.trabajofinal.repository.UserRepository;


@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public JpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

  @Transactional
@Override
public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    if (correo == null || !correo.contains("@")) {
        throw new UsernameNotFoundException("Formato inválido. Use usuario@email.com");
    }

    final UserModel user = repository.findByEmail(correo)
            .orElseThrow(() -> new UsernameNotFoundException(
                    String.format("Usuario con el correo %s no encontrado.", correo)));

    // Se asigna un rol por defecto
    return new CustomUserDetails(
        user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            true,
            true,
            true,
            true,
            java.util.Collections.singletonList(
                    new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")
            )
    );
}

}
