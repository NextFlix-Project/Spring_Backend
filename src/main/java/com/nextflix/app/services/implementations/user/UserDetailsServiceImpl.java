package com.nextflix.app.services.implementations.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nextflix.app.entities.User;
import com.nextflix.app.repositories.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(email);
            }

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    grantedAuthorities);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}