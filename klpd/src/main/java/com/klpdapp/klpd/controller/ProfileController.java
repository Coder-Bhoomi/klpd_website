package com.klpdapp.klpd.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.UserDto;
import com.klpdapp.klpd.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    UserRepo uRepo;

    @Autowired
    CategoryService CategoryService;

    @Autowired
    WishlistRepo wishlistRepo;
    
    @GetMapping
    public String ShowProfile(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            model.addAttribute("user", user);
            CategoryService.addCategoriesToModel(model);
            if (user != null) {
                // Parse the name into firstName, middleName, and lastName
                String[] nameParts = splitName(user.getName());
                UserDto userDto = new UserDto();
                userDto.setUserId(user.getUserId());
                userDto.setDob(user.getDob());
                userDto.setGender(user.getGender());
                userDto.setEmail(user.getEmail());
                userDto.setMobile(user.getMobile());
                userDto.setStatus(user.getStatus());
                userDto.setPassword(user.getPassword());
                // Set parsed name fields
                userDto.setFirstName(nameParts[0]);
                userDto.setMiddleName(nameParts[1]);
                userDto.setLastName(nameParts[2]);

                // Add UserDto to the model
                model.addAttribute("userdto", userDto);
            }
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    private String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[] { "", "", "" };
        }

        String[] parts = fullName.trim().split("\\s+");
        String firstName = parts.length > 0 ? parts[0] : "";
        String middleName = "";
        String lastName = "";

        if (parts.length == 2) {
            lastName = parts[1];
        } else if (parts.length > 2) {
            lastName = parts[parts.length - 1];
            middleName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        }

        return new String[] { firstName, middleName, lastName };
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute UserDto udto, HttpSession session) {
        System.out.println("Received UserDto: " + udto);

        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User existingUser = uRepo.findById(userId).orElse(null);
            System.out.println("Existing user before update: " + existingUser);
            System.out.println("Gender: " + udto.getGender());
            System.out.println("Dob: " + udto.getDob());
            System.out.println("Number: " + udto.getMobile());
            if (existingUser != null) {
                String firstName = udto.getFirstName() != null ? udto.getFirstName() : "";
                String middleName = udto.getMiddleName() != null ? udto.getMiddleName() : "";
                String lastName = udto.getLastName() != null ? udto.getLastName() : "";

                String fullName = firstName;
                if (!middleName.isEmpty()) {
                    fullName += " " + middleName;
                }
                if (!lastName.isEmpty()) {
                    fullName += " " + lastName;
                }

                existingUser.setName(fullName);
                existingUser.setEmail(
                        (udto.getEmail() != null && !udto.getEmail().isEmpty())
                                ? udto.getEmail()
                                : existingUser.getEmail());
                existingUser.setGender(udto.getGender());
                existingUser.setDob(udto.getDob());
                existingUser.setMobile(udto.getMobile());

                uRepo.save(existingUser);

                return "redirect:/profile";
            } else {
                // If the user does not exist, redirect to login
                return "redirect:/login";
            }
        } else {
            // If no session is active, redirect to login
            return "redirect:/login";
        }
    }

    

    @PostMapping("/deactivate")
    public String deactivateAccount(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        user.setStatus("Inactive");
        uRepo.save(user);
        return "redirect:/profile";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteAccount(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userid");
        wishlistRepo.deleteByUser_UserId(userId);
        uRepo.deleteById(userId);
        return "redirect:/login";
    }

    
}
