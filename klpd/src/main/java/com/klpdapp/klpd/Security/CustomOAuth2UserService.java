package com.klpdapp.klpd.Security;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.Repository.UserRepo;
import java.time.LocalDateTime;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepository; 

    @Autowired
    private HttpServletRequest request; 

    @Autowired
    LoginRepo loginRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        HttpSession session = request.getSession();
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub"); // Unique Google ID
        
        // Check if the user already exists
        Optional<Login> existingUser = loginRepository.findByEmail(email);
        
        Login user;
        if (existingUser.isEmpty()) {
            // User doesn't exist, create and save them
            User user2 = new User();
            user2.setName(name);
            user2.setEmail(email);
            user2.setGoogleId(googleId);
            
            userRepository.save(user2); // Save the user to the database

            // Create a new Login entry for the user
            user = new Login();
            user.setName(name);
            user.setEmail(email);
            user.setGoogleId(googleId);

            user.setUserId(user2.getUserId()); // Set the user ID from the User entity
            user.setUserType("Customer"); // Set user type as "user"
            user.setCreatedAt(LocalDateTime.now());
            user.setLastLoginAt(LocalDateTime.now()); // Set last login time
            user.setEnabled(true); // Set user as enabled

            loginRepository.save(user); // Save the login entry to the database
        } else {
            user = existingUser.get();
            // Update last login time
            user.setLastLoginAt(LocalDateTime.now());
            loginRepository.save(user); // Save the updated login entry to the database
        }
        
        // Store user ID in session
        session.setAttribute("userid", user.getId());
        System.out.println("User ID stored in session: " + user.getId());

        // Return OAuth2User with proper roles
        return oauth2User;
    }
}