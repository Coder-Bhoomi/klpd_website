package com.klpdapp.klpd.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.dto.UserDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
public class maincontroller {

    @Autowired
    CategoryRepo ctgRepo;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    CartRepo cartRepository;

    @PersistenceContext
    private EntityManager EntityManager;

    private void addCategoriesToModel(Model model) {
        List<Category> categories = ctgRepo.findAll();
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

        List<Product> Products;

        if (categoryId != null && !categoryId.isEmpty()) {
            Products = pRepo.findByCategory_CategoryId(categoryId);
            Category category = ctgRepo.findById(categoryId).orElse(null);
            model.addAttribute("category", category);

        } else if (query != null && !query.isEmpty()) {

            Session session = EntityManager.unwrap(Session.class);
            Products = Search.session(session)
                    .search(Product.class)
                    .where(f -> f.match().fields("prodName", "brand", "description")
                            .matching(query)
                            .fuzzy())
                    .fetchAllHits();

        } else {
            Products = pRepo.findAll();
        }

        // Sorting
        if ("priceAsc".equals(sortBy)) {
            Products.sort(Comparator.comparing(Product::getMrp));
        } else if ("priceDesc".equals(sortBy)) {
            Products.sort(Comparator.comparing(Product::getMrp).reversed());
        }

        if (color != null && !color.isEmpty()) {
            List<Product> filteredByColor = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getAttribute().getColor() != null
                        && Product.getAttribute().getColor().equalsIgnoreCase(color)) {
                    filteredByColor.add(Product);
                }
            }
            Products = filteredByColor;
        }

        // Filter by discount
        if (minDiscount != null && maxDiscount != null) {
            List<Product> filteredByDiscount = new ArrayList<>();
            for (Product Product : Products) {
                double discount = calculateDiscount(Product.getMrp(), Product.getOfferPrice());
                if (discount >= minDiscount && discount <= maxDiscount) {
                    filteredByDiscount.add(Product);
                }
            }
            Products = filteredByDiscount;
        }
        // Filter by diameter
        if (diameter != null && !diameter.isEmpty()) {
            List<Product> diameterFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getAttribute().getDiameter() != null
                        && Product.getAttribute().getDiameter().equalsIgnoreCase(diameter)) {
                    diameterFiltered.add(Product);
                }
            }
            Products = diameterFiltered;
        }

        // Filter by thickness
        if (thickness != null && !thickness.isEmpty()) {
            List<Product> thicknessFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getAttribute().getThickness() != null
                        && Product.getAttribute().getThickness().equalsIgnoreCase(thickness)) {
                    thicknessFiltered.add(Product);
                }
            }
            Products = thicknessFiltered;
        }

        // Filter by capacity
        if (capacity != null && !capacity.isEmpty()) {
            List<Product> capacityFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getAttribute().getCapacity() != null
                        && Product.getAttribute().getCapacity().equalsIgnoreCase(capacity)) {
                    capacityFiltered.add(Product);
                }
            }
            Products = capacityFiltered;
        }

        // Filter by guarantee
        if (guarantee != null && !guarantee.isEmpty()) {
            List<Product> guaranteeFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getAttribute().getGuarantee() != null
                        && Product.getAttribute().getGuarantee().equalsIgnoreCase(guarantee)) {
                    guaranteeFiltered.add(Product);
                }
            }
            Products = guaranteeFiltered;
        }
        Set<String> colors = new HashSet<>();
        Set<String> diameters = new HashSet<>();
        Set<String> thicknesses = new HashSet<>();
        Set<String> capacities = new HashSet<>();
        Set<String> guarantees = new HashSet<>();

        for (Product Product : Products) {
            if (Product.getAttribute().getColor() != null) {
                colors.add(Product.getAttribute().getColor());
            }
            if (Product.getAttribute().getDiameter() != null) {
                diameters.add(Product.getAttribute().getDiameter());
            }
            if (Product.getAttribute().getThickness() != null) {
                thicknesses.add(Product.getAttribute().getThickness());
            }
            if (Product.getAttribute().getCapacity() != null) {
                capacities.add(Product.getAttribute().getCapacity());
            }
            if (Product.getAttribute().getGuarantee() != null) {
                guarantees.add(Product.getAttribute().getGuarantee());
            }
        }

        // Add data to the model for rendering
        model.addAttribute("products", Products);
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

        Product prod = pRepo.findById(prodId).orElse(null);

        if (prod != null) {
            model.addAttribute("product", prod);
            List<Product> relatedProducts = pRepo.findTop4ByCategoryCategoryIdAndProdIdNot(
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
        List<Cart> cartItems = cartRepository.findAll();
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
        Cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {

            Product Product = cartItem.getProduct();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(
                    quantity * (Product.getOfferPrice() != null ? Product.getOfferPrice() : Product.getMrp()));
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
        
        Product product = pRepo.findById(productId).orElse(null);

        if (product != null) {
            Cart existingCartItem = cartRepository.findByProduct(product);

            if (existingCartItem != null) {
                
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                float price = product.getOfferPrice() != null ? product.getOfferPrice() : product.getMrp();
                existingCartItem.setTotalPrice(price * existingCartItem.getQuantity());

                // Save the updated cart item
                cartRepository.save(existingCartItem);
            } else {
                Cart cart = new Cart();
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

    @GetMapping({"/login"})
    public String ShowLogin(Model model) {
        UserDto dto = new UserDto();
        model.addAttribute("dto",dto);
        return "registration" ;
    }
}