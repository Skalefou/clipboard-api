package org.skalefou.clipboardapi.feature.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.model.Roles;
import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.model.dto.LoginResponseDTO;
import org.skalefou.clipboardapi.feature.repository.RolesRepository;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Transactional
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    private void validateMail(String mail) {
        if (usersRepository.findByMail(mail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mail already used");
        }

        if (!EMAIL_PATTERN.matcher(mail).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid mail format");
        }
    }

    private void validePassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password format");
        }
    }


    public Users registerUser(Users usersProfile, Users users) {
        if (usersProfile != null && usersProfile.getId() != null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already logged in");
        }
        validateMail(users.getMail());
        validePassword(users.getPassword());
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Roles role = rolesService.findByName("USER");
        users.getRoles().add(role);

        users = usersRepository.save(users);
        if (users.getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error in user registration");
        }
        return users;
    }
}
