package com.klpdapp.klpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.klpdapp.klpd.dto.categorydto;
import com.klpdapp.klpd.model.category;
import com.klpdapp.klpd.model.product;
import com.klpdapp.klpd.services.categoryrepo;
import com.klpdapp.klpd.services.productrepo;

@Controller
public class maincontroller {

    @Autowired
    categoryrepo ctgrepo;

    @Autowired
    productrepo prepo;
    private void addCategoriesToModel(Model model) {
        List<category> categories = ctgrepo.findAll();
        System.out.println("Number of categories: " + categories.size());
        for (category cat : categories) {
            System.out.println("Category ID: " + cat.getCategoryId() + ", Name: " + cat.getCategoryName());
        }
        model.addAttribute("categories", categories);
    }
    

   @GetMapping("/home")
    public String showIndex(Model model) {
        addCategoriesToModel(model);
        return "index";  
    }

    @GetMapping("/category/{categoryId}")
    public String showCategory(@ModelAttribute categorydto dto, Model model) {
        category category = ctgrepo.findById(dto.getCategoryId()).orElse(null);

        if (category != null) {
            List<product> products = prepo.findByCategoryId(dto.getCategoryId());
            model.addAttribute("category", category);
            model.addAttribute("products", products);
        }
        addCategoriesToModel(model); 
        return "cooker";  
    } 

    @GetMapping("/cart")
    public String showCart(Model model) {
        addCategoriesToModel(model);
        return "cart";  
    }


}