package org.skalefou.clipboardapi.feature.config;

import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Users user = usersRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        if (user == null) {
            logger.error("User not found with id: " + id);
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        logger.info("User found with id: " + id);
        return new CustomUserDetails(user);
    }
}
