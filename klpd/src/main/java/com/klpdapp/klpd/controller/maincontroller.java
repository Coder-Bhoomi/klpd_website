package com.klpdapp.klpd.controller;

import java.util.*;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.klpdapp.klpd.dto.categorydto;
import com.klpdapp.klpd.dto.productdto;
import com.klpdapp.klpd.model.category;
import com.klpdapp.klpd.model.images;
import com.klpdapp.klpd.model.product;
import com.klpdapp.klpd.services.categoryrepo;
import com.klpdapp.klpd.services.imagesrepo;
import com.klpdapp.klpd.services.productrepo;

@Controller
public class maincontroller {

    @Autowired
    categoryrepo ctgrepo;

    @Autowired
    productrepo prepo;

    @Autowired
    imagesrepo irepo;

    private void addCategoriesToModel(Model model) {
        List<category> categories = ctgrepo.findAll();
        model.addAttribute("categories", categories);
    }

    @GetMapping("/home")
    public String showIndex(Model model) {
        addCategoriesToModel(model);
        return "index";
    }

    @GetMapping("/{categoryId}")
    public String showCategory(@PathVariable String categoryId, Model model) {
        category category = ctgrepo.findById(categoryId).orElse(null);

        if (category != null) {
            List<product> products = prepo.findByCategory_CategoryId(categoryId);

            model.addAttribute("category", category);
            model.addAttribute("products", products);
        }
        addCategoriesToModel(model);

        return "category";
    }
    @GetMapping("/{productId}")
    public String showProductDetails(@PathVariable String productId, Model model) {
    product prod = prepo.findById(productId).orElse(null);
    if (prod != null) {
        model.addAttribute("product", prod);
    }
    return "productDetails"; // Your Thymeleaf view name
}

    @GetMapping("/cart")
    public String showCart(Model model) {
        addCategoriesToModel(model);
        return "cart";
    }

}