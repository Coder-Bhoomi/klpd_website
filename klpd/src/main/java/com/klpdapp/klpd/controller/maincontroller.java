package com.klpdapp.klpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("categories", categories);
    }

    @GetMapping("/home")
    public String showIndex(Model model) {
        addCategoriesToModel(model);
        return "index";
    }

    @GetMapping("{categoryId}")
    public String showCategory(@PathVariable String categoryId, 
    @RequestParam(required = false) String color,
    @RequestParam(required = false) String sortBy,
    Model model) {

    category category = ctgrepo.findById(categoryId).orElse(null);

    if (category != null) {
        List<product> products;

        List<String> colors = prepo.findDistinctColorsByCategory(categoryId);
        model.addAttribute("colors", colors);

        products = prepo.findByCategory_CategoryId(categoryId);

        // Apply color filter 
        if (color != null && !color.isEmpty()) {
            products = prepo.findByCategory_CategoryIdAndAttribute_Color(categoryId, color);
        } 
        
        // Check the sortBy parameter and apply sorting accordingly
        if ("priceAsc".equals(sortBy)) {
            products = prepo.findByCategory_CategoryIdOrderByMrpAsc(categoryId);
        } else if ("priceDesc".equals(sortBy)) {
            products = prepo.findByCategory_CategoryIdOrderByMrpDesc(categoryId);
        }

        model.addAttribute("category", category);
        model.addAttribute("products", products);
    }

    addCategoriesToModel(model);

    return "category";
}

    @GetMapping("/product/{prodId}")
    public String showProductDetails(@PathVariable String prodId, Model model) {
        
        product prod = prepo.findById(prodId).orElse(null);

        if (prod != null) {
            model.addAttribute("product", prod);
            List<product> relatedProducts = prepo.findTop4ByCategoryCategoryIdAndProdIdNot(
            prod.getCategory().getCategoryId(),
            prod.getProdId()
        );
        
        model.addAttribute("relatedProducts", relatedProducts);
        } else {
            model.addAttribute("error", "Product not found!");
        }
        return "product";  
    }
    


    @GetMapping("/cart")
    public String showCart(Model model) {
        addCategoriesToModel(model);
        return "cart";
    }

}