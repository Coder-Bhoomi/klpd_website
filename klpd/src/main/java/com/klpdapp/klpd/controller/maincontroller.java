package com.klpdapp.klpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.model.cart;
import com.klpdapp.klpd.model.category;
import com.klpdapp.klpd.model.product;
import com.klpdapp.klpd.services.cartrepo;
import com.klpdapp.klpd.services.categoryrepo;
import com.klpdapp.klpd.services.productrepo;

@Controller
public class maincontroller {

    @Autowired
    categoryrepo ctgrepo;

    @Autowired
    productrepo prepo;

    @Autowired
    cartrepo cartRepository;

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
                    prod.getProdId());

            model.addAttribute("relatedProducts", relatedProducts);
            model.addAttribute("categoryId", prod.getCategory().getCategoryId());
        } else {
            model.addAttribute("error", "Product not found!");
        }
        return "product";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        // Directly search products based on the query
        List<product> products = prepo.findByProdNameContainingIgnoreCase(query);

        // Add the products to the model to display in the view
        model.addAttribute("products", products);

        addCategoriesToModel(model);
        return "category";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<cart> cartItems = cartRepository.findAll(); // Adjust according to user-specific cart if needed
        float subtotal = cartItems.stream().map(item -> item.getTotalPrice()).reduce(0.0f, Float::sum);
        float discount = 0.0f; // Add logic for any discounts if applicable
        float tax = subtotal * 0.10f; // Assuming 10% tax
        float total = subtotal + tax - discount;

        model.addAttribute("cart", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        addCategoriesToModel(model);
        return "cart"; // Adjust to your Thymeleaf template name
    }

     @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long cartId, @RequestParam Integer quantity, Model model) {
        cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {
            // Update the quantity
            product product = cartItem.getProduct();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * (product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp()));
            cartRepository.save(cartItem); // Save updated cart item
            model.addAttribute("message", "Cart updated successfully.");
        } else {
            model.addAttribute("message", "Cart item not found.");
        }
        return "redirect:/cart"; 
    }

    @GetMapping({ "/cart/delete" })
    public String DeleteCartItem(@RequestParam int id, RedirectAttributes attrib) {
        cartRepository.deleteById((long) id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String productId, @RequestParam Integer quantity, Model model) {
        // Fetch product from the database
        product product = prepo.findById(productId).orElse(null);

        if (product != null) {
            cart existingCartItem = cartRepository.findByProduct(product);

            if (existingCartItem != null) {
                // If product is already in the cart, increase the quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);

                // Update the total price based on the new quantity
                float price = product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp();
                existingCartItem.setTotalPrice(price * existingCartItem.getQuantity());

                // Save the updated cart item
                cartRepository.save(existingCartItem);
            } else {
                // Create a new cart item
                cart cart = new cart();
                cart.setDelivery(60);
                cart.setProduct(product);
                cart.setQuantity(quantity);
                cart.setTotalPrice(product.getOfferPrice() != null ? product.getOfferPrice() * quantity
                        : product.getMrp() * quantity);

                // Save the cart item
                cartRepository.save(cart);

                // Optionally add a success message to the model
                model.addAttribute("message", "Product added to cart!");
            }
        } else {
            // If product not found, show error message
            model.addAttribute("message", "Product not found.");
        }

        // Redirect back to the cart page
        return "redirect:/category"; // Change this URL to your cart page
    }

}