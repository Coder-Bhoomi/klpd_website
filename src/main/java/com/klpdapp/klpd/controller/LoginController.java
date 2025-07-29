package com.klpdapp.klpd.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.wholesalerRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.UserDto;
import com.klpdapp.klpd.dto.WholesellerDto;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wholeseller;
import com.klpdapp.klpd.Security.CustomUserDetailsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    CategoryService CategoryService;

    @Autowired
    UserRepo uRepo;

    @Autowired
    LoginRepo Loginrepo;

    @Autowired
    wholesalerRepo wRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/login")
    public String showLogin(Model model, HttpSession session) {
        UserDto udto = new UserDto();
        model.addAttribute("dto", udto);
        CategoryService.addCategoriesToModel(model);
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "registration";
    }

    @PostMapping("/register")
    public String submitRegister(
            @ModelAttribute UserDto userDto,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            // Encode the password
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());

            // Create a new User object
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setStatus("Active");

            // Save the user in the database
            uRepo.save(user);

            Login login = new Login();
            login.setEmail(userDto.getEmail());
            login.setPassword(encodedPassword);
            login.setUserId(user.getUserId());
            login.setName(userDto.getName());
            login.setCreatedAt(LocalDateTime.now());
            login.setLastLoginAt(LocalDateTime.now()); // Set last login time
            login.setEnabled(true); // Set user as enabled
            login.setUserType("Customer");

            Loginrepo.save(login);

            //Authentication manually
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("userid", login.getId());

            redirectAttributes.addFlashAttribute("message", "Registered Successfully!");
            return "redirect:/profile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong!");
            return "redirect:/";
        }
    }

    @GetMapping("/wholesaler")
    public String wholesalerLogin(Model model, HttpSession session) {
        WholesellerDto udto = new WholesellerDto();
        model.addAttribute("dto", udto);
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "wholesaler";
    }

    @PostMapping("/registerwholesale") 
    public String wholesaleRegister(@ModelAttribute WholesellerDto wholesellerDto, RedirectAttributes redirectAttributes) {

        try {
            // Encode the password
            String encodedPassword = passwordEncoder.encode(wholesellerDto.getPassword());

            // Create a new wholesaler object
            Wholeseller wholesaler = new Wholeseller();
            wholesaler.setName(wholesellerDto.getName());
            wholesaler.setCompanyName(wholesellerDto.getCompanyName());
            wholesaler.setGSTIN(wholesellerDto.getGSTIN());
            wholesaler.setOfficeAddress(wholesellerDto.getOfficeAddress());
            wholesaler.setShippingAddress(wholesellerDto.getShippingAddress());
            wholesaler.setContactNumber(wholesellerDto.getContactNumber());
            wholesaler.setEmail(wholesellerDto.getEmail());
            wholesaler.setOrganisationNumber(wholesellerDto.getOrganisationNumber());

            // Save the wholesaler in the database
            wRepo.save(wholesaler);

            Login login = new Login();
            login.setEmail(wholesellerDto.getEmail());
            login.setPassword(encodedPassword);
            login.setUserId(wholesaler.getWholesellerId());
            login.setCreatedAt(LocalDateTime.now());
            login.setUserType("Wholesaler");

            Loginrepo.save(login);

            redirectAttributes.addFlashAttribute("message", "Registered Successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong!");
        }
        return "redirect:/"; // Redirect 
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

}
