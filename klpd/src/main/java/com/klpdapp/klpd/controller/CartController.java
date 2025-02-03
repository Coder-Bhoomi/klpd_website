package com.klpdapp.klpd.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Services.CartService;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    UserRepo uRepo;

    @Autowired
    com.klpdapp.klpd.Services.CategoryService CategoryService;

    @Autowired
    CartRepo cartRepository;

    @Autowired
    ProductRepo pRepo;

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartService.getCartItems(user);
            float subtotal = cartItems.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
            float discount = 0.0f;
            float tax = subtotal * 0.10f;
            float total = subtotal + tax - discount;

            model.addAttribute("cart", cartItems);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("discount", discount);
            model.addAttribute("tax", tax);
            model.addAttribute("total", total);
            CategoryService.addCategoriesToModel(model);
            return "cart";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Integer cartId, @RequestParam Integer quantity, Model model) {
        Cart cartItem = cartRepository.getById(cartId);
        if (cartItem != null) {

            Product Product = cartItem.getProduct();
            cartItem.setQuantity(quantity);
            cartItem.setProductTotal(
                    quantity * (Product.getOfferPrice() != null ? Product.getOfferPrice() : Product.getMrp()));
            cartRepository.save(cartItem);
            model.addAttribute("message", "Cart updated successfully.");
        }
        return "redirect:/cart";
    }

    @DeleteMapping({ "/cart/delete" })
    public String DeleteCartItem(@RequestParam int id, RedirectAttributes attrib) {
        cartService.deleteCartItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String Checkout(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("userid") != null) {
                Integer userId = (Integer) session.getAttribute("userid");
                User user = uRepo.findById(userId).orElse(null);
                cartService.checkout(user);
                return "redirect:/";
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(HttpSession session, @RequestParam Integer productId, @RequestParam Integer quantity,
            Model model) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            Product product = pRepo.findById(productId).orElse(null);

            if (product != null) {
                Cart existingCartItem = cartRepository.findByProductAndUser(product, user).orElse(null);

                if (existingCartItem != null) {
                    // Update quantity and product total
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                    float price = (product.getOfferPrice() != null) ? product.getOfferPrice() : product.getMrp();
                    existingCartItem.setProductTotal(price * existingCartItem.getQuantity());
                    cartRepository.save(existingCartItem);
                } else {
                    // Create a new cart item
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setProduct(product);
                    cart.setQuantity(quantity);
                    float price = (product.getOfferPrice() != null) ? product.getOfferPrice() : product.getMrp();
                    cart.setProductTotal(price * quantity);
                    cartRepository.save(cart);
                }

                model.addAttribute("message", "Product added to cart!");
            } else {
                model.addAttribute("message", "Product not found.");
            }
        } else {
            return "redirect:/login";
        }

        return "redirect:/cart";

    }

    
}
