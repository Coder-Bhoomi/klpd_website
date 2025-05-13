package com.klpdapp.klpd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.PincodeRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.AddressDto;
import com.klpdapp.klpd.model.Address;
import com.klpdapp.klpd.model.Pincode;
import com.klpdapp.klpd.model.Login;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressRepo addressrepo;

    @Autowired
    CategoryService CategoryService;

    @Autowired
    UserRepo uRepo;

    @Autowired
    PincodeRepo pincodeRepo;
    
    @Autowired
    LoginRepo loginRepo;

    @GetMapping
    public String ShowAddress(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login user = loginRepo.findById(userId).orElse(null);
            if (user != null) {
                AddressDto aDto = new AddressDto();
                aDto.setUserId(user.getUserId());
                model.addAttribute("aDto", aDto);
            }
            List<Address> address = addressrepo.findByUser(user);
            model.addAttribute("address", address);
            model.addAttribute("user", user);
            CategoryService.addCategoriesToModel(model);
            return "address";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/add")
    public String AddAddress(Model model, HttpSession session, @ModelAttribute AddressDto aDto,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login user = loginRepo.findById(userId).orElse(null);
            if (user != null) {

                Optional<Pincode> pincode = pincodeRepo.findByPincode((Integer) aDto.getPincode());
                if (pincode.isPresent()) {
                    // Pincode exists, proceed to save the address
                    Address a = new Address();
                    a.setUser(user);
                    a.setNumber(aDto.getNumber());
                    a.setAddress(aDto.getAddress());
                    a.setName(aDto.getName());
                    a.setPincode(aDto.getPincode());
                    a.setCity(aDto.getCity());
                    a.setState(aDto.getState());
                    a.setCountry(aDto.getCountry());
                    a.setLandmark(aDto.getLandmark());
                    addressrepo.save(a);
                    return "redirect:/address";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Pincode not found. Please enter a valid pincode.");
                    return "redirect:/address";
                }
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String AddressDelete(@RequestParam("addressid") int addressId, RedirectAttributes attrib) {
        addressrepo.deleteById(addressId);
        return "redirect:/address";

    }

    
}
