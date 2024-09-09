package org.skalefou.clipboardapi.feature.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.model.Roles;
import org.skalefou.clipboardapi.feature.repository.RolesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class RolesService {
    private final RolesRepository rolesRepository;

    public Roles findByName(String name) {
        Optional<Roles> role = rolesRepository.findByName(name);
        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error in role management");
        }
        return role.get();
    }
}
