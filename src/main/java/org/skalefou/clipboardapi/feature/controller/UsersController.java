package org.skalefou.clipboardapi.feature.controller;

import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.config.users.InjectUser;
import org.skalefou.clipboardapi.feature.config.security.JwtService;
import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.model.dto.AuthRequestDTO;
import org.skalefou.clipboardapi.feature.model.dto.LoginResponseDTO;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.skalefou.clipboardapi.feature.service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    private final UsersService usersService;

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
    public LoginResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getId(), authRequestDTO.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(UUID.fromString(authRequestDTO.getId()))).build();
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @InjectUser
    @PostMapping("/public/user/register")
    public LoginResponseDTO registerUser(Users usersProfile, @RequestBody Users users) {
        users = usersService.registerUser(usersProfile, users);
        return LoginResponseDTO.builder()
                .id(users.getId())
                .mail(users.getMail())
                .registrationDate(users.getRegistrationDate())
                .accessToken(jwtService.generateToken(users.getId()))
                .build();
    }

    @InjectUser
    @PostMapping("/public/github/login")
    public void loginWithGithub(Users usersProfile) {
/*        if (usersProfile.getId() != null) {
            throw new UsernameNotFoundException("Invalid user request");
        }
        usersService.loginWithGithub(usersProfile, users);*/
    }
}
