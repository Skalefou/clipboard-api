package org.skalefou.clipboardapi.feature.controller;

import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.config.JwtService;
import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.model.dto.AuthRequestDTO;
import org.skalefou.clipboardapi.feature.model.dto.JwtResponseDTO;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsersController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ping")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/public/user/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getMail(), authRequestDTO.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getMail())).build();
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
