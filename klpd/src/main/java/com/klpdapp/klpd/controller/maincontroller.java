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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.klpdapp.klpd.Repository.CouponRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.PincodeRepo;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.WishlistRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.AdminDto;
import com.klpdapp.klpd.model.Admin;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.User;
import com.klpdapp.klpd.model.Wishlist;

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
    EntityManager EntityManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CategoryService CategoryService;

    private static List<Product> Products;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static void addFilter(Model model) {
        Set<String> colors = new HashSet<>();
        Set<String> diameters = new HashSet<>();
        Set<String> thicknesses = new HashSet<>();
        Set<String> capacities = new HashSet<>();
        Set<String> guarantees = new HashSet<>();
        Set<String> brand = new HashSet<>();

        for (Product product : Products) {
            if (product.getColor() != null) {
                colors.add(product.getColor().replace("-", ""));
            }
            if (product.getDiameter() != null) {
                diameters.add(product.getDiameter().replace("-", "")); 
            }
            if (product.getThickness() != null) {
                thicknesses.add(product.getThickness().replace("-", "")); // Remove hyphen 
            }
            if (product.getCapacity() != null) {
                capacities.add(product.getCapacity().replace("-", ""));
            }
            if (product.getGuarantee() != null) {
                guarantees.add(product.getGuarantee().replace("-", ""));
            }
            if (product.getBrand() != null) {
                brand.add(product.getBrand().replace("-", "")); 
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
    public String showIndex(Model model, HttpSession session) {
        List<Product> NewProducts = pRepo.findTop4ByOrderByCreatedAtDesc();
        List<Product> TopProducts = pRepo.findTop4ByOrderByHitsDesc();
        CategoryService.addCategoriesToModel(model);
        model.addAttribute("topProduct", TopProducts);
        model.addAttribute("newProduct", NewProducts);
        Set<Integer> cartProductIds = new HashSet<>();
        Set<Integer> wishlistProductIds = new HashSet<>();
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId != null) {
            User user = uRepo.findById(userId).orElse(null);
            List<Cart> cartItems = cartRepository.findByUser(user);
            for (Cart item : cartItems) {
                cartProductIds.add(item.getProduct().getPid()); 
            }        
            model.addAttribute("cartItem", cartItems);
            List<Wishlist> wishlistItems = wishlistRepo.findAllByUser(user);
            for (Wishlist item : wishlistItems) {
                wishlistProductIds.add(item.getProduct().getPid()); 
            }        
        }
        model.addAttribute("cartProductIds", cartProductIds);
        model.addAttribute("wishlistProductIds", wishlistProductIds);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model, HttpSession usersession) {
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
                                        .boost(10.0f)) 
                                .should(f.match()
                                        .fields("prodName", "brand")
                                        .matching(query)
                                        .fuzzy()
                                        .boost(5.0f)) 
                                .should(f.match()
                                        .fields("brand", "description")
                                        .matching(query)
                                        .boost(4.0f)) // Exact match on brand and description with lower boost
                                .should(f.simpleQueryString()
                                        .fields("prodName")
                                        .matching(query + "*")
                                        .boost(3.0f)) // Prefix match to capture terms starting with the query
                        )
                        .fetchAllHits()
                        .stream()
                        .distinct() 
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
        CategoryService.addCategoriesToModel(model);
        Integer userId = (Integer) usersession.getAttribute("userid");
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
            Model model, HttpSession session) {

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
        if (Products != null && !Products.isEmpty()) {
            Category category = Products.get(0).getCategory();
            model.addAttribute("category", category);
            model.addAttribute("query", query);

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
        Products = p;
        addFilter(model);
        CategoryService.addCategoriesToModel(model);

        return "category";
    }

    @GetMapping("/category/{categoryId}")
    public String showCategory(@PathVariable String categoryId, Model model, HttpSession session) {
        if (categoryId != null && !categoryId.isEmpty()) {
            Products = pRepo.findByCategory_CategoryId(categoryId);
            Category category = ctgRepo.findById(categoryId).orElse(null);
            model.addAttribute("category", category);
            model.addAttribute("products", Products);
            CategoryService.addCategoriesToModel(model);
            addFilter(model);
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
                model.addAttribute("wishlistProductIds", wishlistProductIds);            }
        }
        return "category";
    }

    @GetMapping("/coupon")
    public String ShowCoupon(Model model, HttpSession session) {
        CategoryService.addCategoriesToModel(model);
        Integer userId = (Integer) session.getAttribute("userid");
        User user = uRepo.findById(userId).orElse(null);
        List<Coupon> coupon = couponrepo.findAll();
        model.addAttribute("coupon", coupon);
        model.addAttribute("user", user);
        return "coupon";
    }

    @GetMapping("/order")
    public String ShowOrder(Model model, HttpSession session) {
        CategoryService.addCategoriesToModel(model);
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
        CategoryService.addCategoriesToModel(model);
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

    
    @GetMapping({ "/admin" })
    public String ShowAdmLogin(Model model) {
        AdminDto addto = new AdminDto();
        model.addAttribute("dto", addto);
        CategoryService.addCategoriesToModel(model);
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
