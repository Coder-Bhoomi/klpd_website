package com.klpdapp.klpd.Security;

import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.Repository.UserRepo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }        
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("User is disabled!");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),  // Enabled
                true,              // Account non-expired
                true,              // Credentials non-expired
                true,              // Account non-locked
                new ArrayList<>()  // Authorities
        );
    }
}
