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

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.Services.CartService;
import com.klpdapp.klpd.Services.CouponService;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Address;

import jakarta.servlet.http.HttpServletRequest;
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
    CategoryService CategoryService;

    @Autowired
    CartRepo cartRepository;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    CouponService couponService;

    @Autowired
    LoginRepo loginRepo;

    @Autowired
    AddressRepo addRepo;

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login user = loginRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartService.getCartItems(user);
            float subtotal = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum);
            float discount = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum)
                    - cartItems.stream()
                            .map(item -> item.getProductTotal())
                            .reduce(0.0f, Float::sum);
            float coupondiscount = 0.0f;
            float total = subtotal - coupondiscount - discount;
            Coupon appliedCoupon = (Coupon) session.getAttribute("coupon");
            Float appliedcoupondiscount = (Float) session.getAttribute("coupondiscount");
            Float appliedTotal = (Float) session.getAttribute("total");
            if (appliedcoupondiscount != null) {
                coupondiscount = appliedcoupondiscount;
                total = appliedTotal;
                model.addAttribute("coupon", appliedCoupon);
            }
            model.addAttribute("cart", cartItems);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("discount", discount);
            model.addAttribute("coupondiscount", coupondiscount);
            model.addAttribute("total", total);
            CategoryService.addCategoriesToModel(model);
            model.addAttribute("usertype", user.getUserType());
            return "cart";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/apply-coupon")
    public String applyCoupon(@RequestParam String couponCode, Model model, HttpSession session) {
        Coupon coupon = couponService.getCouponByCode(couponCode).orElse(null);
        Integer userId = (Integer) session.getAttribute("userid");
        Login user = loginRepo.findById(userId).orElse(null);
        List<Cart> cartItems = cartService.getCartItems(user);
        float subtotal = cartItems.stream()
                .map(item -> item.getQuantity() * item.getProduct().getMrp())
                .reduce(0.0f, Float::sum);
        float discount = cartItems.stream()
                .map(item -> item.getQuantity() * item.getProduct().getMrp())
                .reduce(0.0f, Float::sum)
                - cartItems.stream()
                        .map(item -> item.getProductTotal())
                        .reduce(0.0f, Float::sum);
        int items = cartItems.size();
        if (coupon.getMinQuantity() <= items & coupon.getMinCartValue() <= subtotal) {
            float coupondiscount = 0.0f;
            float coupondiscountbypercentage = (cartItems.stream()
                    .map(item -> item.getProductTotal())
                    .reduce(0.0f, Float::sum) * coupon.getDiscountRate()) / 100;
            if (coupon.getUptoAmount() > 0) {
                coupondiscount = Math.min(coupondiscountbypercentage, coupon.getUptoAmount());
            } else {
                coupondiscount = coupondiscountbypercentage;
            }
            float total = subtotal - coupondiscount - discount;
            session.setAttribute("cart", cartItems);
            session.setAttribute("subtotal", subtotal);
            session.setAttribute("discount", discount);
            session.setAttribute("coupondiscount", coupondiscount);
            session.setAttribute("total", total);
            session.setAttribute("coupon", coupon);
        } else {
            model.addAttribute("message", "Coupon not applicable.");
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(HttpServletRequest request, @RequestParam Integer cartId,
            @RequestParam(required = false) String action, @RequestParam Integer quantity, Model model,
            HttpSession session) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);
        if (cartItem != null) {
            if ("minus".equals(action) && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            } else if ("plus".equals(action)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else {
                cartItem.setQuantity(quantity);
            }
            Product Product = cartItem.getProduct();
            cartItem.setProductTotal(
                    cartItem.getQuantity()
                            * (Product.getOfferPrice() != null ? Product.getOfferPrice() : Product.getMrp()));
            cartRepository.save(cartItem);
            model.addAttribute("message", "Cart updated successfully.");
        }
        Integer userId = (Integer) session.getAttribute("userid");
        Login user = loginRepo.findById(userId).orElse(null);
        List<Cart> cartItems = cartService.getCartItems(user);
        // Calculate subtotal, coupondiscount, tax, and total
        float subtotal = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum);
            float discount = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum)
                    - cartItems.stream()
                            .map(item -> item.getProductTotal())
                            .reduce(0.0f, Float::sum);
            float coupondiscount = 0.0f;
            float total = subtotal - coupondiscount - discount;
        model.addAttribute("cart", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("coupondiscount", coupondiscount);
        model.addAttribute("total", total);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping({ "/delete" })
    public String DeleteCartItem(@RequestParam int id, RedirectAttributes attrib) {
        cartService.deleteCartItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addToCart(HttpSession session, @RequestParam Integer productId, @RequestParam Integer quantity,
            Model model) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login user = loginRepo.findById(userId).orElse(null);
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

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            Login user = loginRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartService.getCartItems(user);
            float subtotal = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum);
            float discount = cartItems.stream()
                    .map(item -> item.getQuantity() * item.getProduct().getMrp())
                    .reduce(0.0f, Float::sum)
                    - cartItems.stream()
                            .map(item -> item.getProductTotal())
                            .reduce(0.0f, Float::sum);
            float coupondiscount = 0.0f;
            float total = subtotal - coupondiscount - discount;
            Coupon appliedCoupon = (Coupon) session.getAttribute("coupon");
            Float appliedcoupondiscount = (Float) session.getAttribute("coupondiscount");
            Float appliedTotal = (Float) session.getAttribute("total");
            Float appliedsubtotal = (Float) session.getAttribute("subtotal");
            if (appliedcoupondiscount != null) {
                coupondiscount = appliedcoupondiscount;
                total = appliedTotal;
                subtotal = appliedsubtotal;
                if (appliedCoupon != null) {
                    model.addAttribute("coupon", appliedCoupon);
                }
            }
            model.addAttribute("cart", cartItems);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("discount", discount);
            model.addAttribute("coupondiscount", coupondiscount);
            model.addAttribute("total", total);
            CategoryService.addCategoriesToModel(model);
            List<Address> address = addRepo.findByUser(user);
            model.addAttribute("address", address);
            return "checkout";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/checkout")
    public String Checkout(HttpSession session, HttpServletResponse response,
            @RequestParam String paymentMode, @RequestParam int selectedAddress) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("userid") != null) {
                Integer userId = (Integer) session.getAttribute("userid");
                Login user = loginRepo.findById(userId).orElse(null);
                Float couponDiscount = (Float) session.getAttribute("coupondiscount");
                float discount = couponDiscount != null ? couponDiscount : 0.0f;
            
                cartService.checkout(user, paymentMode, selectedAddress, discount);
                return "redirect:/";
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
