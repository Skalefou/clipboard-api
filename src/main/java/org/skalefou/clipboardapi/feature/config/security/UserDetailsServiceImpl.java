package org.skalefou.clipboardapi.feature.config.security;

import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Users user = usersRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + id));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with mail:  " + id);
        }
        return new CustomUserDetails(user);
    }
}
