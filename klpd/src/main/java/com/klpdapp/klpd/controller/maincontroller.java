package com.klpdapp.klpd.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.search.mapper.orm.Search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.dto.UserDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;

@Controller
public class maincontroller {

    @Autowired
    CategoryRepo ctgRepo;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    CartRepo cartRepository;

    @Autowired
    UserRepo uRepo;

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

    @GetMapping("/")
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
            try {
                Search.session(EntityManager.unwrap(Session.class))
                        .massIndexer()
                        .startAndWait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                Session session = EntityManager.unwrap(Session.class);
                Products = Search.session(session)
                        .search(Product.class)
                        .where(f -> f.bool()
                                .should(f.match()
                                        .fields("prodName")
                                        .matching(query)
                                        .boost(5.0f)) // Exact match on prodName with the highest boost
                                .should(f.match()
                                        .fields("brand", "description")
                                        .matching(query)
                                        .boost(4.0f)) // Exact match on brand and description with lower boost
                                .should(f.simpleQueryString()
                                        .fields("prodName")
                                        .matching(query + "*")
                                        .boost(3.0f)) // Prefix match to capture terms starting with the query
                                .should(f.match()
                                        .fields("prodName", "brand", "description")
                                        .matching(query)
                                        .fuzzy()
                                        .boost(1.0f)) // Fuzzy match for variations and typos
                        )
                        .fetchAllHits()
                        .stream()
                        .distinct() // Ensures that only unique products are returned
                        .collect(Collectors.toList());
            }
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
                if (Product.getColor() != null
                        && Product.getColor().equalsIgnoreCase(color)) {
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
                if (Product.getDiameter() != null
                        && Product.getDiameter().equalsIgnoreCase(diameter)) {
                    diameterFiltered.add(Product);
                }
            }
            Products = diameterFiltered;
        }

        // Filter by thickness
        if (thickness != null && !thickness.isEmpty()) {
            List<Product> thicknessFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getThickness() != null
                        && Product.getThickness().equalsIgnoreCase(thickness)) {
                    thicknessFiltered.add(Product);
                }
            }
            Products = thicknessFiltered;
        }

        // Filter by capacity
        if (capacity != null && !capacity.isEmpty()) {
            List<Product> capacityFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getCapacity() != null
                        && Product.getCapacity().equalsIgnoreCase(capacity)) {
                    capacityFiltered.add(Product);
                }
            }
            Products = capacityFiltered;
        }

        // Filter by guarantee
        if (guarantee != null && !guarantee.isEmpty()) {
            List<Product> guaranteeFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getGuarantee() != null
                        && Product.getGuarantee().equalsIgnoreCase(guarantee)) {
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
            if (Product.getColor() != null) {
                colors.add(Product.getColor());
            }
            if (Product.getDiameter() != null) {
                diameters.add(Product.getDiameter());
            }
            if (Product.getThickness() != null) {
                thicknesses.add(Product.getThickness());
            }
            if (Product.getCapacity() != null) {
                capacities.add(Product.getCapacity());
            }
            if (Product.getGuarantee() != null) {
                guarantees.add(Product.getGuarantee());
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

    @GetMapping("/product/{pid}")
    public String showProductDetails(@PathVariable Integer pid, Model model) {

        Product prod = pRepo.getById(pid);

        if (prod != null) {
            model.addAttribute("product", prod);
            List<Product> relatedProducts = pRepo.findTop4ByCategoryCategoryIdAndPidNot(
                    prod.getCategory().getCategoryId(),
                    prod.getPid());

            model.addAttribute("relatedProducts", relatedProducts);
            model.addAttribute("categoryId", prod.getCategory().getCategoryId());
        } 
        addCategoriesToModel(model);

        return "product";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            List<Cart> cartItems = cartRepository.findAll();
            float subtotal = cartItems.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
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
        } else {
            return "redirect/login";
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
        } else {
            model.addAttribute("message", "Cart item not found.");
        }
        return "redirect:/cart";
    }

    @GetMapping({ "/cart/delete" })
    public String DeleteCartItem(@RequestParam int id, RedirectAttributes attrib) {
        cartRepository.deleteById( id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(HttpSession session, @RequestParam Integer productId, @RequestParam Integer quantity,
            Model model) {
        if (session.getAttribute("userid") != null) {
            Product product = pRepo.findById(productId).orElse(null);

            if (product != null) {
                Cart existingCartItem = cartRepository.findByProduct(product);

                if (existingCartItem != null) {

                    existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                    float price = (product.getOfferPrice() != null) ? product.getOfferPrice() : product.getMrp();
                    existingCartItem.setProductTotal((int)price * existingCartItem.getQuantity());

                    // Save the updated cart item
                    cartRepository.save(existingCartItem);
                } else {
                    Cart cart = new Cart();
                    cart.setProduct(product);
                    cart.setQuantity(quantity);
                    cart.setProductTotal(product.getOfferPrice() != null ? product.getOfferPrice() * quantity
                            : product.getMrp() * quantity);

                    // Save the cart item
                    cartRepository.save(cart);

                    model.addAttribute("message", "Product added to cart!");
                }
            } else {
                model.addAttribute("message", "Product not found.");
            }
            return "redirect:/cart";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping({ "/login" })
    public String ShowLogin(Model model) {
        UserDto udto = new UserDto();
        model.addAttribute("dto", udto);
        addCategoriesToModel(model);
        return "registration";
    }

    @PostMapping("/submit")
    public String handleFormSubmission(
            @ModelAttribute UserDto userDto,
            @RequestParam("actionType") String actionType,
            HttpSession session,
            RedirectAttributes attrib) {

        if ("login".equals(actionType)) {
            return validateLogin(userDto, session, attrib); // Delegate to login logic
        } else if ("register".equals(actionType)) {
            return SubmitRegister(userDto, attrib); // Delegate to registration logic
        } else {
            attrib.addFlashAttribute("msg", "Invalid Action Type");
            return "redirect:/";
        }
    }

    public String validateLogin(@ModelAttribute UserDto udto, HttpSession session, RedirectAttributes attrib) {
        try {
            User us = uRepo.findByEmail(udto.getEmail());
            if (us.getPassword().equals(udto.getPassword())) {
                session.setAttribute("userid", us.getUserId());
                return "redirect:/home";
            } else {
                attrib.addFlashAttribute("msg", "Invalid User");
            }
            return "redirect:/login";
        } catch (EntityNotFoundException ex) {
            attrib.addFlashAttribute("msg", "User doesn't exist!!");
            return "redirect:/login";
        }
    }

    public String SubmitRegister(@ModelAttribute("dto") UserDto userDto,
            RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setStatus("Active");
            uRepo.save(user);
            redirectAttributes.addFlashAttribute("message", "Registered Successfully");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong!");
            return "redirect:/register";
        }
    }

}