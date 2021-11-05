package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.repositories.UsersRepository;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + username + " not found"));

        return new CustomUserDetails(user);
    }
}
