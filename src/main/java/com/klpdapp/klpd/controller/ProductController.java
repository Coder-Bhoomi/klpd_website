package com.klpdapp.klpd.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.ReviewRepository;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.Services.ProductService;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Review;
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

    @Autowired
    LoginRepo loginRepo;

    @Autowired
    ReviewRepository reviewrepo;

    @Autowired
    OrderItemRepository orderItemRepo;
    
    @GetMapping("/{pid}")
    public String showProductDetails(@PathVariable Integer pid, @RequestParam(required = false) String selectedSize,
            @RequestParam(required = false) String selectedSubcategoryId,
            Model model, HttpSession session) {
        Product prod = productService.findById(pid);

        // Increasing hits on product
        productService.incrementProductHits(prod.getPid());

        // Check if size and subcategory are selected
        if (selectedSize != null && !selectedSize.isEmpty() && selectedSubcategoryId != null) {
            boolean isInductionBase = false;
            if (prod.getProdName().toLowerCase().contains("induction")) {
                isInductionBase = true;
            }
            // Get the products based on size 
            List<Product> products = isInductionBase
            ? pRepo.findInductionProductsBySizeAndSubcategory(selectedSize, selectedSubcategoryId)
            : pRepo.findNonInductionProductsBySizeAndSubcategory(selectedSize, selectedSubcategoryId);
            // print the products
            System.out.println("Products found: " + products.size());


            if (!products.isEmpty()) {
                prod = (products.get(0));  
            }
            return "redirect:/product/" + prod.getPid();
        } else {
            prod = pRepo.findById(pid).orElse(null);
        }

        if (prod != null) {
            model.addAttribute("product", prod);

            // Get the related products to the current product in same subcategory
            List<Product> relatedProducts = pRepo.findTop4BySubcategorySubcategoryIdAndPidNot(
                    prod.getSubcategory().getSubcategoryId(),
                    prod.getPid());

            boolean isInductionBase = false;

            // Check if the current product name contains "induction"
            if (prod.getProdName().toLowerCase().contains("induction")) {
                isInductionBase = true;
            }
            // Get the sizes of the product
            List<String> sizes = isInductionBase 
                ? pRepo.findInductionSizesbySubcategory(prod.getSubcategory().getSubcategoryId())
                : pRepo.findNonInductionSizesbySubcategory(prod.getSubcategory().getSubcategoryId());
            
            sizes.removeIf(size -> size.trim().equals("-")); // Remove hyphens from the list
            model.addAttribute("sizes", sizes);

            model.addAttribute("relatedProducts", relatedProducts);
            model.addAttribute("categoryId", prod.getSubcategory().getCategory().getCategoryId());
        }

        //Review and rating part
        List<Review> reviews = reviewrepo.findReviewWithText(prod);
        model.addAttribute("reviews", reviews);
        Integer numberOfReview = reviewrepo.countByProduct(prod);
        model.addAttribute("numberOfReview", numberOfReview);
        List<Object[]> ratingDistribution = reviewrepo.getRatingDistributionByProduct(prod);
        Map<Integer, Integer> ratingMap = new HashMap<>();
        for (Object[] obj : ratingDistribution) {
            Integer rating = (Integer) obj[0];
            Long count = (Long) obj[1];
            ratingMap.put(rating, count.intValue());
        }
        Map<Integer, Double> ratingDist = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Double percentage = ratingMap.containsKey(i) ? 
                 (ratingMap.get(i) * 100.0 / numberOfReview) : 00;
            ratingDist.put(i, percentage);
        }
        model.addAttribute("ratingDistribution", ratingDist);
        
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId != null) {
            Login user = loginRepo.findById(userId).orElse(null);
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
    
    @GetMapping("/{pid}/review")
    public String showReviewForm(@PathVariable("pid") int pid, Model model,HttpSession session) {
        // Check if user is logged in
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }
        // Get current user
        Login user = loginRepo.findById(userId).orElseThrow();
        Product product = productService.findById(pid);
        //Check if the user had already ordered product
        if( orderItemRepo.existsByOrder_UserAndProduct(user, product)) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "You can only review products you have purchased.");
        }
        model.addAttribute("product", product);
        return "review";
    }
    
    @PostMapping("/{pid}/review")
    public String submitReview(@PathVariable("pid") int pid,
                             @RequestParam("rating") int rating,
                             @RequestParam(value = "comment", required = false) String comment,
                            HttpSession session) {
        // Get current user
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }
        Login user = loginRepo.findById(userId).orElseThrow();
        
        // Get product
        Product product = productService.findById(pid);

        // Create and save review
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setDate(LocalDate.now());
        
        reviewrepo.save(review);

        Integer numberOfReview = reviewrepo.countByProduct(product);
        Float totalRating = reviewrepo.getTotalRatingByProduct(product);
        Float averageRating = totalRating / numberOfReview;
        product.setRating(averageRating);
        productService.save(product);

        return "redirect:/product/" + pid;
    }

}