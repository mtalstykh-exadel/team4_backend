package com.team4.testingsystem.security;

import com.team4.testingsystem.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final String name;
    private final String login;
    private final String password;
    private final Collection<SimpleGrantedAuthority> roles;

    public CustomUserDetails(User user) {
        this.name = user.getName();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.roles = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public String getName() {
        return name;
    }
}
