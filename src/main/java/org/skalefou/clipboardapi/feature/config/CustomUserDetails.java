package org.skalefou.clipboardapi.feature.config;

import org.skalefou.clipboardapi.feature.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends Users implements UserDetails {
    private String id;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(final Users users) {
        this.id = users.getId().toString();
        this.password = users.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
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
