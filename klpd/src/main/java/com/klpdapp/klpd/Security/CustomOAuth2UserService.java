package com.klpdapp.klpd.Security;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepository; 

    @Autowired
    private HttpServletRequest request; 

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        HttpSession session = request.getSession();
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub"); // Unique Google ID
        
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        User user;
        if (existingUser.isEmpty()) {
            // User doesn't exist, create and save them
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setGoogleId(googleId);
            
            userRepository.save(user); // Save the user to the database
        } else {
            user = existingUser.get();
        }
        
        // Store user ID in session
        session.setAttribute("userid", user.getUserId());

        // Return OAuth2User with proper roles
        return oauth2User;
    }
}