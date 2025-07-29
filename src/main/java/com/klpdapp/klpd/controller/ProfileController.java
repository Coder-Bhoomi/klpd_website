package com.klpdapp.klpd.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Repository.wholesalerRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.UserDto;
import com.klpdapp.klpd.dto.WholesellerDto;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wholeseller;

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

    @Autowired
    LoginRepo Loginrepo;

    @Autowired
    wholesalerRepo wRepo;

    @GetMapping
    public String ShowProfile(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login loginuser = Loginrepo.findById(userId).orElse(null);
            CategoryService.addCategoriesToModel(model);
            if (loginuser.getUserType().equals("Wholesaler")) {
                Wholeseller wholesaler = wRepo.findById(loginuser.getUserId()).orElse(null);
                model.addAttribute("user", wholesaler);
                WholesellerDto wholesalerDto = new WholesellerDto();
                wholesalerDto.setWholesellerId(wholesaler.getWholesellerId());
                wholesalerDto.setName(wholesaler.getName());
                wholesalerDto.setCompanyName(wholesaler.getCompanyName());
                wholesalerDto.setGSTIN(wholesaler.getGSTIN());
                wholesalerDto.setOfficeAddress(wholesaler.getOfficeAddress());
                wholesalerDto.setShippingAddress(wholesaler.getShippingAddress());
                wholesalerDto.setContactNumber(wholesaler.getContactNumber());
                wholesalerDto.setEmail(wholesaler.getEmail());
                wholesalerDto.setOrganisationNumber(wholesaler.getOrganisationNumber());

                model.addAttribute("wdto", wholesalerDto);
                return "wholesalerprofile";

            } else if (loginuser.getUserType().equals("Customer")) {
                User user = uRepo.findById(loginuser.getUserId()).orElse(null);
                model.addAttribute("user", user);
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
                    userDto.setPassword(loginuser.getPassword());

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
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login loginuser = Loginrepo.findByUserId(userId).orElse(null);
            User existingUser = uRepo.findById(loginuser.getUserId()).orElse(null);
            if (existingUser != null) {
                // Handle name
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
                existingUser.setName(fullName.trim().isEmpty() ? existingUser.getName() : fullName);

                // Only update fields that were actually provided in the form
                if (udto.getEmail() != null && !udto.getEmail().isEmpty()) {
                    existingUser.setEmail(udto.getEmail());
                }

                if (udto.getGender() != null) {
                    existingUser.setGender(udto.getGender());
                }

                if (udto.getDob() != null) {
                    existingUser.setDob(udto.getDob());
                }

                existingUser.setSpouseDob(udto.getSpouseDob()); // can be null
                existingUser.setAnniversary(udto.getAnniversary()); // can be null
                existingUser.setChildName(udto.getChildName());
                existingUser.setChildDob(udto.getChildDob()); // can be null

                if (udto.getMobile() != 0) { // assuming 0 is not a valid mobile number
                    existingUser.setMobile(udto.getMobile());
                }

                uRepo.save(existingUser);
                return "redirect:/profile";
            }
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    @PostMapping("/updatewholesaler")
    public String updateWholesalerProfile(@ModelAttribute WholesellerDto wdto, HttpSession session) {

        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login loginuser = Loginrepo.findById(userId).orElse(null);
            Wholeseller existingWholesaler = wRepo.findById(loginuser.getUserId()).orElse(null);
            if (existingWholesaler != null) {
                existingWholesaler.setName(wdto.getName());
                existingWholesaler.setCompanyName(wdto.getCompanyName());
                existingWholesaler.setGSTIN(wdto.getGSTIN());
                existingWholesaler.setOfficeAddress(wdto.getOfficeAddress());
                existingWholesaler.setShippingAddress(wdto.getShippingAddress());
                existingWholesaler.setContactNumber(wdto.getContactNumber());
                existingWholesaler.setEmail(wdto.getEmail());
                existingWholesaler.setOrganisationNumber(wdto.getOrganisationNumber());

                wRepo.save(existingWholesaler);

                return "redirect:/profile";
            } else {
                // If the wholesaler does not exist, redirect to login
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
