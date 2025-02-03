package com.klpdapp.klpd.Security;

import com.klpdapp.klpd.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

     @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });
        HttpSession session = request.getSession();
        session.setAttribute("userid", user.getUserId());
        return new CustomUserDetails(user); 
    }
    
}
