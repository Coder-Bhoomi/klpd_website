package com.klpdapp.klpd.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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

import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.Repository.AdminRepo;
import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.PincodeRepo;
import com.klpdapp.klpd.Repository.CouponRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.SizeRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.model.Address;
import com.klpdapp.klpd.model.Admin;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Pincode;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wishlist;
import com.klpdapp.klpd.dto.AddressDto;
import com.klpdapp.klpd.dto.AdminDto;
import com.klpdapp.klpd.dto.UserDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

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

    @Autowired
    SizeRepo sizeRepo;

    @Autowired
    AdminRepo adRepo;

    @Autowired
    WishlistRepo wishlistRepo;

    @Autowired
    OrderRepository orderrepo;

    @Autowired
    OrderItemRepository orderitemrepo;

    @Autowired
    PincodeRepo pincoderepo;

    @Autowired
    AddressRepo addressrepo;

    @Autowired
    CouponRepo couponrepo;

    @PersistenceContext
    private EntityManager EntityManager;

    private static List<Product> Products;

    private void addCategoriesToModel(Model model) {
        List<Category> categories = ctgRepo.findAll();
        categories.sort(Comparator.comparing(Category::getCategoryName));

        // Find and remove "Parts & Accessories" and "Others" from the list if they
        // exist
        Category partsAndAccessories = null;
        Category others = null;

        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()) {
            Category category = iterator.next();
            if ("Parts & Accessories".equalsIgnoreCase(category.getCategoryName())) {
                partsAndAccessories = category;
                iterator.remove();
            } else if ("Others".equalsIgnoreCase(category.getCategoryName())) {
                others = category;
                iterator.remove();
            }
        }

        // Add "Parts & Accessories" and "Others" to the end of the list
        if (partsAndAccessories != null) {
            categories.add(partsAndAccessories);
        }
        if (others != null) {
            categories.add(others);
        }
        // Add the modified categories list to the model
        model.addAttribute("categories", categories);
    }

    private static void addFilter(Model model) {
        Set<String> colors = new HashSet<>();
        Set<String> diameters = new HashSet<>();
        Set<String> thicknesses = new HashSet<>();
        Set<String> capacities = new HashSet<>();
        Set<String> guarantees = new HashSet<>();
        Set<String> brand = new HashSet<>();

        for (Product product : Products) {
            if (product.getColor() != null) {
                colors.add(product.getColor().replace("-", "")); // Remove hyphen from color
            }
            if (product.getDiameter() != null) {
                diameters.add(product.getDiameter().replace("-", "")); // Remove hyphen from diameter
            }
            if (product.getThickness() != null) {
                thicknesses.add(product.getThickness().replace("-", "")); // Remove hyphen from thickness
            }
            if (product.getCapacity() != null) {
                capacities.add(product.getCapacity().replace("-", "")); // Remove hyphen from capacity
            }
            if (product.getGuarantee() != null) {
                guarantees.add(product.getGuarantee().replace("-", "")); // Remove hyphen from guarantee
            }
            if (product.getBrand() != null) {
                brand.add(product.getBrand().replace("-", "")); // Remove hyphen from guarantee
            }
        }

        // Sort the filter sets by converting them to lists
        List<String> sortedColors = new ArrayList<>(colors);
        sortedColors.sort(String::compareToIgnoreCase); // Sort alphabetically (case-insensitive)

        List<String> sortedDiameters = new ArrayList<>(diameters);
        sortedDiameters.sort(String::compareToIgnoreCase);

        List<String> sortedThicknesses = new ArrayList<>(thicknesses);
        sortedThicknesses.sort(String::compareToIgnoreCase);

        List<String> sortedCapacities = new ArrayList<>(capacities);
        sortedCapacities.sort(String::compareToIgnoreCase);

        List<String> sortedGuarantees = new ArrayList<>(guarantees);
        sortedGuarantees.sort(String::compareToIgnoreCase);
        List<String> sortedBrand = new ArrayList<>(brand);
        sortedBrand.sort(String::compareToIgnoreCase);

        // Add data to the model for rendering
        model.addAttribute("colors", sortedColors);
        model.addAttribute("diameters", sortedDiameters);
        model.addAttribute("thicknesses", sortedThicknesses);
        model.addAttribute("capacities", sortedCapacities);
        model.addAttribute("guarantees", sortedGuarantees);
        model.addAttribute("brands", sortedBrand);

    }

    private double calculateDiscount(double mrp, double offerPrice) {
        if (mrp > 0 && offerPrice > 0) {
            return ((mrp - offerPrice) / mrp) * 100;
        }
        return 0;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        List<Product> NewProducts = pRepo.findTop4ByOrderByCreatedAtDesc();
        List<Product> TopProducts = pRepo.findTop4ByOrderByHitsDesc();
        addCategoriesToModel(model);
        model.addAttribute("topProduct", TopProducts);
        model.addAttribute("newProduct", NewProducts);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
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
                model.addAttribute("products", Products);
                addFilter(model);
            }
        } else {
            List<Product> Products = pRepo.findAll();
            model.addAttribute("products", Products);
            addFilter(model);

        }
        model.addAttribute("query", query);
        addCategoriesToModel(model);
        return "category";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) Integer maxDiscount,
            @RequestParam(required = false) String diameter,
            @RequestParam(required = false) String thickness,
            @RequestParam(required = false) String capacity,
            @RequestParam(required = false) String guarantee,
            @RequestParam(required = false) String brand,
            Model model) {

        List<Product> p = Products;

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
        if (brand != null && !brand.isEmpty()) {
            List<Product> brandFiltered = new ArrayList<>();
            for (Product Product : Products) {
                if (Product.getBrand() != null
                        && Product.getBrand().equalsIgnoreCase(brand)) {
                    brandFiltered.add(Product);
                }
            }
            Products = brandFiltered;
        }
        // Add data to the model for rendering
        model.addAttribute("products", Products);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("minDiscount", minDiscount);
        model.addAttribute("maxDiscount", maxDiscount);
        Category category = ctgRepo.findById(categoryId).orElse(null);
        model.addAttribute("category", category);
        model.addAttribute("query", query);

        Products = p;
        addFilter(model);
        addCategoriesToModel(model);

        return "category";
    }

    @GetMapping("/category/{categoryId}")
    public String showCategory(@PathVariable String categoryId, Model model) {
        if (categoryId != null && !categoryId.isEmpty()) {
            Products = pRepo.findByCategory_CategoryId(categoryId);
            Category category = ctgRepo.findById(categoryId).orElse(null);
            model.addAttribute("category", category);
            model.addAttribute("products", Products);
            addCategoriesToModel(model);
            addFilter(model);
        }
        return "category";
    }

    @GetMapping("/product/{pid}")
    public String showProductDetails(@PathVariable Integer pid, @RequestParam(required = false) String selectedSize,
            Model model) {
        Product prod = pRepo.getById(pid);

        if (prod != null) {
            model.addAttribute("product", prod);
            List<Product> relatedProducts = pRepo.findTop4ByCategoryCategoryIdAndPidNot(
                    prod.getCategory().getCategoryId(),
                    prod.getPid());

            List<String> sizes = pRepo.findDistinctSizesBySubcategoryId(prod.getSubcategory().getSubcategoryId());

            /*
             * Fetch products based on size
             * 
             * if (selectedSize != null) {
             * prod =
             * pRepo.findProductsBySizeAndSubcategory(prod.getSubcategory().getSubcategoryId
             * (), selectedSize);
             * } else {
             * prod =
             * pRepo.findBySubcategory_SubcategoryId(prod.getSubcategory().getSubcategoryId(
             * )); // All products for the
             * // subcategory
             * }
             */
            model.addAttribute("sizes", sizes);

            model.addAttribute("relatedProducts", relatedProducts);
            model.addAttribute("categoryId", prod.getCategory().getCategoryId());
        }
        addCategoriesToModel(model);

        return "product";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartRepository.findByUser(user);
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

    @GetMapping({ "/cart/delete" })
    public String DeleteCartItem(@RequestParam int id, RedirectAttributes attrib) {
        cartRepository.deleteById(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String Checkout(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("userid") != null) {
                Integer userId = (Integer) session.getAttribute("userid");
                User user = uRepo.findById(userId).orElse(null);
                Order order = new Order();
                List<Cart> carts = cartRepository.findByUser(user);
                float subtotal = carts.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
                float discount = 0.0f;
                float tax = subtotal * 0.10f;
                int total = (int) (subtotal + tax - discount);
                order.setUser(user);
                order.setTotalAmt(total);
                order.setOrderDate(LocalDate.now());
                orderrepo.save(order);
                for (Cart cart : carts) {
                    OrderItem orderitem = new OrderItem();
                    orderitem.setOrder(order);
                    orderitem.setProdQuantity(cart.getQuantity());
                    orderitem.setProduct(cart.getProduct());
                    orderitemrepo.save(orderitem);
                }
                cartRepository.deleteByUser(user);
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

    @GetMapping("/wishlist")
    public String showwishlist(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            List<Wishlist> wishlistItems = wishlistRepo.findAllByUser(user);
            model.addAttribute("wishlist", wishlistItems);
            model.addAttribute("user", user);
            addCategoriesToModel(model);
            return "wishlist";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping({ "/wishlist/delete" })
    public String DeletewishlistItem(@RequestParam("wishlistId") int wishlistId, RedirectAttributes attrib) {
        wishlistRepo.deleteById(wishlistId);
        return "redirect:/wishlist";
    }

    @PostMapping("/wishlist/add")
    public String addTowishlist(HttpSession session, @RequestParam Integer productId,
            Model model) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            Product product = pRepo.findById(productId).orElse(null);

            if (product != null) {
                Wishlist existingwishlistItem = wishlistRepo.findByProductAndUser(product, user).orElse(null);

                if (existingwishlistItem == null) {
                    // Create a new wishlist item
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    wishlist.setProduct(product);
                    wishlistRepo.save(wishlist);
                    System.out.println("added");
                }

                model.addAttribute("message", "Product added to wishlist!");
            } else {
                model.addAttribute("message", "Product not found.");
            }
        } else {
            return "redirect:/login";
        }

        return "redirect:/wishlist";

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
            return SubmitRegister(userDto, attrib, session); // Delegate to registration logic
        } else {
            attrib.addFlashAttribute("msg", "Invalid Action Type");
            return "redirect:/";
        }
    }

    public String validateLogin(@ModelAttribute UserDto udto, HttpSession session, RedirectAttributes attrib) {
        try {
            User us = uRepo.findByEmail(udto.getEmail());
            if (us == null) {
                attrib.addFlashAttribute("msg", "User doesn't exist!!");
                return "redirect:/login";
            }
            if (us.getPassword().equals(udto.getPassword())) {
                attrib.addFlashAttribute("msg", "Valid User");
                session.setAttribute("userid", us.getUserId());
                return "redirect:/profile";
            } else {
                attrib.addFlashAttribute("msg", "Invalid User");
                return "redirect:/profile";
            }
        } catch (EntityNotFoundException ex) {
            attrib.addFlashAttribute("msg", "User doesn't exist!!");
            return "redirect:/login";
        }
    }

    public String SubmitRegister(@ModelAttribute UserDto userDto,
            RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setStatus("Active");
            uRepo.save(user);
            session.setAttribute("userid", userDto.getUserId());
            redirectAttributes.addFlashAttribute("message", "Registered Successfully");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong!");
            return "redirect:/";
        }
    }

    @GetMapping("/profile")
    public String ShowProfile(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            model.addAttribute("user", user);
            addCategoriesToModel(model);
            if (user != null) {
                // Parse the name into firstName, middleName, and lastName
                String[] nameParts = splitName(user.getName());
                UserDto userDto = new UserDto();
                userDto.setUserId(user.getUserId());
                userDto.setDob(user.getDob());
                userDto.setGender(user.getGender());
                userDto.setEmail(user.getEmail());
                userDto.setMobile(user.getMobile());
                userDto.setStatus(user.getStatus());
                userDto.setPassword(user.getPassword());
                // Set parsed name fields
                userDto.setFirstName(nameParts[0]);
                userDto.setMiddleName(nameParts[1]);
                userDto.setLastName(nameParts[2]);

                // Add UserDto to the model
                model.addAttribute("userdto", userDto);
            }
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    private String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[] { "", "", "" };
        }

        String[] parts = fullName.trim().split("\\s+");
        String firstName = parts.length > 0 ? parts[0] : "";
        String middleName = "";
        String lastName = "";

        if (parts.length == 2) {
            lastName = parts[1];
        } else if (parts.length > 2) {
            lastName = parts[parts.length - 1];
            middleName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        }

        return new String[] { firstName, middleName, lastName };
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserDto udto, HttpSession session) {
        System.out.println("Received UserDto: " + udto);

        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User existingUser = uRepo.findById(userId).orElse(null);
            System.out.println("Existing user before update: " + existingUser);
            System.out.println("Gender: " + udto.getGender());
            System.out.println("Dob: " + udto.getDob());
            System.out.println("Number: " + udto.getMobile());
            if (existingUser != null) {
                String firstName = udto.getFirstName() != null ? udto.getFirstName() : "";
                String middleName = udto.getMiddleName() != null ? udto.getMiddleName() : "";
                String lastName = udto.getLastName() != null ? udto.getLastName() : "";

                String fullName = firstName;
                if (!middleName.isEmpty()) {
                    fullName += " " + middleName;
                }
                if (!lastName.isEmpty()) {
                    fullName += " " + lastName;
                }

                existingUser.setName(fullName);
                existingUser.setEmail(
                        (udto.getEmail() != null && !udto.getEmail().isEmpty())
                                ? udto.getEmail()
                                : existingUser.getEmail());
                existingUser.setGender(udto.getGender());
                existingUser.setDob(udto.getDob());
                existingUser.setMobile(udto.getMobile());

                uRepo.save(existingUser);

                return "redirect:/profile";
            } else {
                // If the user does not exist, redirect to login
                return "redirect:/login";
            }
        } else {
            // If no session is active, redirect to login
            return "redirect:/login";
        }
    }

    @GetMapping("/address")
    public String ShowAddress(Model model, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);
            if (user != null) {
                AddressDto aDto = new AddressDto();
                aDto.setUserId(user.getUserId());
                model.addAttribute("aDto", aDto);
            }
            List<Address> address = addressrepo.findByUser(user);
            model.addAttribute("address", address);
            model.addAttribute("user", user);
            addCategoriesToModel(model);
            return "address";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/address/add")
    public String AddAddress(Model model, HttpSession session, @ModelAttribute AddressDto aDto, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userid") != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            User user = uRepo.findById(userId).orElse(null);

            if (user != null) {

                Optional<Pincode> pincode = pincoderepo.findByPincode((Integer) aDto.getPincode());
                if (pincode.isPresent()) {
                    // Pincode exists, proceed to save the address
                    Address a = new Address();
                    a.setUser(user);
                    a.setNumber(aDto.getNumber());
                    a.setAddress(aDto.getAddress());
                    a.setName(aDto.getName());
                    a.setPincode(aDto.getPincode());

                    addressrepo.save(a);
                    return "redirect:/address";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Pincode not found. Please enter a valid pincode.");
                    return "redirect:/address";
                }
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/coupon")
    public String ShowCoupon(Model model, HttpSession session) {
        addCategoriesToModel(model);
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        List<Coupon> coupon = couponrepo.findAll();
        model.addAttribute("coupon", coupon);
        model.addAttribute("user", user);
        return "coupon";
    }

    @GetMapping("/order")
    public String ShowOrder(Model model, HttpSession session) {
        addCategoriesToModel(model);
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        List<OrderItem> orderItems = orderitemrepo.findByOrder_User(user);
        model.addAttribute("orderitems", orderItems);
        model.addAttribute("user", user);
        return "order";
    }

    @GetMapping("/notification")
    public String ShowNotification(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        model.addAttribute("user", user);
        addCategoriesToModel(model);
        return "notification";
    }

    @GetMapping({ "/logout" })
    public String Logout(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        model.addAttribute("user", user);
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @PostMapping("/profile/deactivate")
    public String deactivateAccount(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        user.setStatus("Inactive");
        uRepo.save(user);
        return "redirect:/profile";
    }

    @Transactional
    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userid");
        wishlistRepo.deleteByUser_UserId(userId);
        uRepo.deleteById(userId);
        return "redirect:/login";
    }

    @GetMapping({ "/admin" })
    public String ShowAdmLogin(Model model) {
        AdminDto addto = new AdminDto();
        model.addAttribute("dto", addto);
        addCategoriesToModel(model);
        return "/admin/login";
    }

    @PostMapping("/admin")
    public String validateAdmLogin(@ModelAttribute AdminDto adto, HttpSession session, RedirectAttributes attrib) {
        try {
            Admin adm = adRepo.findByEmail(adto.getEmail());
            if (adm.getPassword().equals(adto.getPassword())) {
                session.setAttribute("userid", adm.getEmail());
                return "redirect:/admin/dashboard";
            } else {
                attrib.addFlashAttribute("msg", "Invalid User");
            }
            return "redirect:/admin";
        } catch (EntityNotFoundException ex) {
            attrib.addFlashAttribute("msg", "User doesn't exist!!");
            return "redirect:/";
        }
    }
}
