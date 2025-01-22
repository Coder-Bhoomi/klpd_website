package com.klpdapp.klpd.Security;

import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.Repository.UserRepo;

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to find user by email: " + email);
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
            System.out.println("User not found for email: " + email);
            return new UsernameNotFoundException("User not found");
        });
            
        System.out.println("User found: " + user.getEmail());
        return new CustomUserDetails(user); 
    }
    
}
