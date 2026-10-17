
package com.leonardo.listapi.service;

import com.leonardo.listapi.dto.RegisterRequest;
import com.leonardo.listapi.entity.User;
import com.leonardo.listapi.exception.EmailAlreadyExistsException;
import com.leonardo.listapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leonardo.listapi.dto.LoginRequest;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "Este e-mail já está cadastrado"
            );
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }


    public User authenticate(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.authentication
                        .BadCredentialsException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new org.springframework.security.authentication
                    .BadCredentialsException("E-mail ou senha inválidos");
        }

        return user;
    }

}
