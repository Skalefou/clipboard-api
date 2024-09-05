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

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Users user = usersRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + mail));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with mail:  " + mail);
        }
        return new CustomUserDetails(user);
    }
}
