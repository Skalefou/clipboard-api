package org.skalefou.clipboardapi.feature.config.security;

import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.model.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends Users implements UserDetails {
    private String mail;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Users user) {
        this.mail = user.getMail();
        this.password = user.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(Roles role : user.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public String getUsername() {
        return this.mail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
