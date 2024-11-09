package com.klpdapp.klpd.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private double calculateDiscount(double mrp, double offerPrice) {
        if (mrp > 0 && offerPrice > 0) {
            return ((mrp - offerPrice) / mrp) * 100;
        }
        return 0;
    }

    @GetMapping("/home")
    public String showIndex(Model model) {
        addCategoriesToModel(model);
        return "index";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String query,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) Integer maxDiscount,
            @RequestParam(required = false) String diameter,
            @RequestParam(required = false) String thickness,
            @RequestParam(required = false) String capacity,
            @RequestParam(required = false) String guarantee,
            Model model) {

        List<product> products;

        if (categoryId != null && !categoryId.isEmpty()) {
            products = prepo.findByCategory_CategoryId(categoryId);
            category category = ctgrepo.findById(categoryId).orElse(null);
            model.addAttribute("category", category);

        } else if (query != null && !query.isEmpty()) {
            products = prepo.findByProdNameContainingIgnoreCase(query);
        } else {
            products = prepo.findAll();
        }

        // Sorting
        if ("priceAsc".equals(sortBy)) {
            products.sort(Comparator.comparing(product::getMrp));
        } else if ("priceDesc".equals(sortBy)) {
            products.sort(Comparator.comparing(product::getMrp).reversed());
        }

        if (color != null && !color.isEmpty()) {
            List<product> filteredByColor = new ArrayList<>();
            for (product product : products) {
                if (product.getAttribute().getColor() != null
                        && product.getAttribute().getColor().equalsIgnoreCase(color)) {
                    filteredByColor.add(product);
                }
            }
            products = filteredByColor;
        }

        // Filter by discount 
        if (minDiscount != null && maxDiscount != null) {
            List<product> filteredByDiscount = new ArrayList<>();
            for (product product : products) {
                double discount = calculateDiscount(product.getMrp(), product.getOfferPrice());
                if (discount >= minDiscount && discount <= maxDiscount) {
                    filteredByDiscount.add(product);
                }
            }
            products = filteredByDiscount;
        }
        // Filter by diameter
        if (diameter != null && !diameter.isEmpty()) {
            List<product> diameterFiltered = new ArrayList<>();
            for (product product : products) {
                if (product.getAttribute().getDiameter() != null
                        && product.getAttribute().getDiameter().equalsIgnoreCase(diameter)) {
                    diameterFiltered.add(product);
                }
            }
            products = diameterFiltered;
        }

        // Filter by thickness
        if (thickness != null && !thickness.isEmpty()) {
            List<product> thicknessFiltered = new ArrayList<>();
            for (product product : products) {
                if (product.getAttribute().getThickness() != null
                        && product.getAttribute().getThickness().equalsIgnoreCase(thickness)) {
                    thicknessFiltered.add(product);
                }
            }
            products = thicknessFiltered;
        }

        // Filter by capacity
        if (capacity != null && !capacity.isEmpty()) {
            List<product> capacityFiltered = new ArrayList<>();
            for (product product : products) {
                if (product.getAttribute().getCapacity() != null
                        && product.getAttribute().getCapacity().equalsIgnoreCase(capacity)) {
                    capacityFiltered.add(product);
                }
            }
            products = capacityFiltered;
        }

        // Filter by guarantee
        if (guarantee != null && !guarantee.isEmpty()) {
            List<product> guaranteeFiltered = new ArrayList<>();
            for (product product : products) {
                if (product.getAttribute().getGuarantee() != null
                        && product.getAttribute().getGuarantee().equalsIgnoreCase(guarantee)) {
                    guaranteeFiltered.add(product);
                }
            }
            products = guaranteeFiltered;
        }
        Set<String> colors = new HashSet<>();
        Set<String> diameters = new HashSet<>();
        Set<String> thicknesses = new HashSet<>();
        Set<String> capacities = new HashSet<>();
        Set<String> guarantees = new HashSet<>();

        for (product product : products) {
            if (product.getAttribute().getColor() != null) {
                colors.add(product.getAttribute().getColor());
            }
            if (product.getAttribute().getDiameter() != null) {
                diameters.add(product.getAttribute().getDiameter());
            }
            if (product.getAttribute().getThickness() != null) {
                thicknesses.add(product.getAttribute().getThickness());
            }
            if (product.getAttribute().getCapacity() != null) {
                capacities.add(product.getAttribute().getCapacity());
            }
            if (product.getAttribute().getGuarantee() != null) {
                guarantees.add(product.getAttribute().getGuarantee());
            }
        }

        // Add data to the model for rendering
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("colors", colors);
        model.addAttribute("diameters", diameters);
        model.addAttribute("thicknesses", thicknesses);
        model.addAttribute("capacities", capacities);
        model.addAttribute("guarantees", guarantees);
        model.addAttribute("minDiscount", minDiscount);
        model.addAttribute("maxDiscount", maxDiscount);

        addCategoriesToModel(model);

        return "category"; 
    }

    @GetMapping("{categoryId}")
    public String showCategory(@PathVariable String categoryId, Model model) {
        return "redirect:/products?categoryId=" + categoryId;
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
        addCategoriesToModel(model);

        return "product";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<cart> cartItems = cartRepository.findAll(); 
        float subtotal = cartItems.stream().map(item -> item.getTotalPrice()).reduce(0.0f, Float::sum);
        float discount = 0.0f; 
        float tax = subtotal * 0.10f; 
        float total = subtotal + tax - discount;

        model.addAttribute("cart", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        addCategoriesToModel(model);
        return "cart"; 
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long cartId, @RequestParam Integer quantity, Model model) {
        cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {
            
            product product = cartItem.getProduct();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(
                    quantity * (product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp()));
            cartRepository.save(cartItem); 
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
        
        product product = prepo.findById(productId).orElse(null);

        if (product != null) {
            cart existingCartItem = cartRepository.findByProduct(product);

            if (existingCartItem != null) {
                
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                float price = product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp();
                existingCartItem.setTotalPrice(price * existingCartItem.getQuantity());

                // Save the updated cart item
                cartRepository.save(existingCartItem);
            } else {
                cart cart = new cart();
                cart.setDelivery(60);
                cart.setProduct(product);
                cart.setQuantity(quantity);
                cart.setTotalPrice(product.getOfferPrice() != null ? product.getOfferPrice() * quantity
                        : product.getMrp() * quantity);

                // Save the cart item
                cartRepository.save(cart);

                model.addAttribute("message", "Product added to cart!");
            }
        } else {
            model.addAttribute("message", "Product not found.");
        }
        return "redirect:/cart";
    }

}