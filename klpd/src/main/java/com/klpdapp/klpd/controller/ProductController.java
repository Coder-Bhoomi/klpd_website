package com.klpdapp.klpd.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.Services.ProductService;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wishlist;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    ProductRepo pRepo;

    @Autowired
    UserRepo uRepo;

    @Autowired
    CartRepo cartRepository;

    @Autowired
    WishlistRepo wishlistRepo;

    @Autowired
    CategoryService CategoryService;

    @Autowired
    ProductService productService;
    
    @GetMapping("/{pid}")
    public String showProductDetails(@PathVariable Integer pid, @RequestParam(required = false) String selectedSize,
            @RequestParam(required = false) String selectedSubcategoryId,
            Model model, HttpSession session) {
        Product prod = pRepo.findById(pid).orElse(null);

        // Increasing hits on product
        productService.incrementProductHits(prod.getPid());

        // Check if size and subcategory are selected
        if (selectedSize != null && !selectedSize.isEmpty() && selectedSubcategoryId != null) {
            boolean isInductionBase = false;

            if (prod.getProdName().toLowerCase().contains("induction")) {
                isInductionBase = true;
            }
            List<Product> products; 
            // fetch the product based on size and subcatory with respect to induction or non-induction
            if (isInductionBase) {
                products = pRepo.findInductionProductsBySizeAndSubcategory(selectedSize, selectedSubcategoryId);
            } else {
                products = pRepo.findNonInductionProductsBySizeAndSubcategory(selectedSize, selectedSubcategoryId);
            }

            if (!products.isEmpty()) {
                prod = (products.get(0));  
            }
        } else {
            prod = pRepo.findById(pid).orElse(null);
        }

        if (prod != null) {
            model.addAttribute("product", prod);

            // Get the related products to the current product in same subcategory
            List<Product> relatedProducts = pRepo.findTop4BySubcategorySubcategoryIdAndPidNot(
                    prod.getSubcategory().getSubcategoryId(),
                    prod.getPid());

            List<String> sizes;

            boolean isInductionBase = false;

            // Check if the current product name contains "induction"
            if (prod.getProdName().toLowerCase().contains("induction")) {
                isInductionBase = true;
            }

            if (isInductionBase) {
                // Fetch induction-based product sizes
                sizes = pRepo.findInductionSizesbySubcategory(prod.getSubcategory().getSubcategoryId());
            } else {
                // Fetch non-induction products sizes
                sizes = pRepo.findNonInductionSizesbySubcategory(prod.getSubcategory().getSubcategoryId());
            }
            
            sizes.removeIf(size -> size.trim().equals("-")); // Remove hyphens from the list
            model.addAttribute("sizes", sizes);

            model.addAttribute("relatedProducts", relatedProducts);
            model.addAttribute("categoryId", prod.getCategory().getCategoryId());
        }
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId != null) {
            User user = uRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartRepository.findByUser(user);
            model.addAttribute("cart", cartItems);
            List<Wishlist> wishlistItems = wishlistRepo.findAllByUser(user);
            Set<Integer> wishlistProductIds = new HashSet<>();
            for (Wishlist item : wishlistItems) {
                wishlistProductIds.add(item.getProduct().getPid()); 
            }        
            model.addAttribute("wishlistProductIds", wishlistProductIds);        }
        CategoryService.addCategoriesToModel(model);

        return "product";
    }
}
