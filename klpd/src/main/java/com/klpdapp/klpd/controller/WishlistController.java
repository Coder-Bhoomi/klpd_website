package com.klpdapp.klpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wishlist;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    UserRepo uRepo;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    CategoryService CategoryService;

    @Autowired
    WishlistRepo wishlistRepo;

    @GetMapping //("/wishlist")
    public String showwishlist(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            List<Wishlist> wishlistItems = wishlistRepo.findAllByUser(user);
            model.addAttribute("wishlist", wishlistItems);
            model.addAttribute("user", user);
            CategoryService.addCategoriesToModel(model);
            return "wishlist";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping({ "/delete" })
    public String DeletewishlistItem(@RequestParam("wishlistId") int wishlistId, RedirectAttributes attrib) {
        wishlistRepo.deleteById(wishlistId);
        return "redirect:/wishlist";
    }

    @PostMapping("/add")
    public String addTowishlist(HttpSession session, @RequestParam Integer productId,
            Model model) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            Product product = pRepo.findById(productId).orElse(null);

            if (product != null) {
                Wishlist existingwishlistItem = wishlistRepo.findByProductAndUser(product, user).orElse(null);

                if (existingwishlistItem == null) {
                    // Create a new wishlist item
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    wishlist.setProduct(product);
                    wishlistRepo.save(wishlist);
                    System.out.println("added");
                }

                model.addAttribute("message", "Product added to wishlist!");
            } else {
                model.addAttribute("message", "Product not found.");
            }
        } else {
            return "redirect:/login";
        }

        return "redirect:/wishlist";

    }


}
