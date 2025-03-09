package com.klpdapp.klpd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.AdminRepo;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.CouponRepo;
import com.klpdapp.klpd.Repository.ImagesRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.SubCategoryRepo;
import com.klpdapp.klpd.dto.ProductDto;
import com.klpdapp.klpd.model.Admin;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.Images;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.SubCategory;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "/admin" })
public class AdminController {

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	CategoryRepo Catrepo;

	@Autowired
	SubCategoryRepo sCatRepo;

	@Autowired
	ProductRepo prepo;

	@Autowired
	ImagesRepo imgRepo;

	@Autowired
	AdminRepo adrepo;

	@Autowired
	CouponRepo cRepo;

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	AdminRepo adRepo;

	@GetMapping({ "/dashboard" })
	public String showIndex(Model model) {
		List<Product> topsales =prepo.findTop4ByOrderBySalesDesc();
		model.addAttribute("topsales", topsales);
		List<Product> lessStock = prepo.findTop4ByOrderByStockAsc();
		model.addAttribute("lessStock", lessStock);
		return "admin/dashboard";
	}

	@GetMapping("/order")
	public String showOrders(Model model) {
		List<Order> orders = orderRepo.findAll();
		model.addAttribute("Orders", orders);
		return "admin/order";
	}

	@GetMapping({ "/category" })
	public String ShowCategories(Model model) {
		List<Category> categ = Catrepo.findAll();
		model.addAttribute("category", categ);
		return "admin/category";
	}

	@PostMapping("/category/update")
	public String updateCategory(@ModelAttribute Category category) {
		Catrepo.save(category);
		return "redirect:/admin/category";
	}

	@GetMapping({ "/sub-category" })
	public String ShowSubCategory(Model model) {
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);
		return "admin/sub_category";
	}

	@PostMapping("/sub-category/update")
	public String updateSubCategory(@ModelAttribute SubCategory subCategory, @RequestParam String categoryId) {
		Category category = Catrepo.findById(categoryId).orElse(null);
		subCategory.setCategory(category);

		sCatRepo.save(subCategory);

		return "redirect:/admin/sub-category";
	}

	@GetMapping({ "/product" })
	public String ShowProduct(Model model) {
		ProductDto pdto = new ProductDto();
		model.addAttribute("productDto", pdto);
		List<Category> categ = Catrepo.findAll();
		model.addAttribute("category", categ);
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);

		return "admin/product";
	}

	@GetMapping({ "/productlist" })
	public String ShowProductList(Model model) {
		List<Product> p = prepo.findAll();
		model.addAttribute("product", p);
		return "admin/productlist";
	}

	@GetMapping("/productdetail/{pid}")
	public String ShowProductDetail(Model model, @PathVariable Integer pid) {
		Product prod = prepo.findById(pid).orElse(null);
		model.addAttribute("product", prod);
		List<Category> categ = Catrepo.findAll();
		model.addAttribute("category", categ);
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);
		return "admin/productdetail";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product) {
		prepo.save(product); // Save updated product details
		return "redirect:/productdetail/" + product.getPid();
	}

	@GetMapping({ "/coupon" })
	public String ShowCoupon(Model model) {
		List<Coupon> c = cRepo.findAll();
		model.addAttribute("coupon", c);
		return "admin/coupon";
	}

	@GetMapping({ "/setting" })
	public String ShowSetting(Model model) {
		return "admin/setting";
	}

	@PostMapping("/addCategory")
	public String addCategory(@RequestParam("categoryName") String categoryName,
			@RequestParam("categoryId") String categoryId) {
		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategoryName(categoryName);
		Catrepo.save(category);
		return "redirect:/admin/product";
	}

	@PostMapping("/addSubCategory")
	public String addSubCategory(@RequestParam("SubCategoryId") String SubCategoryId,
			@RequestParam("CategoryId") String CategoryId,
			@RequestParam("SubCategoryName") String SubCategoryName) {
		SubCategory subCat = new SubCategory();
		subCat.setSubcategoryId(SubCategoryId);
		subCat.setSubcategoryName(SubCategoryName);
		Category cat = Catrepo.findById(CategoryId).orElse(null);
		subCat.setCategory(cat);
		sCatRepo.save(subCat);
		return "redirect:/admin/product";
	}

	@PostMapping("/addNewProduct")
	public String addNewProduct(@ModelAttribute ProductDto prodDto,
			@RequestParam("secondaryImageInput") List<MultipartFile> secondaryImgURL,
			@RequestParam("PrimaryImage") MultipartFile primaryImgURL) throws IOException {
		System.out.println("Secondary img length=" + secondaryImgURL.size());
		Product prod = new Product();
		prod.setCompanyPid(prodDto.getCompanyPid());
		prod.setHapPid(prodDto.getHapPid());
		Category cat = Catrepo.findById(prodDto.getCategory()).orElse(null);
		prod.setCategory(cat);
		SubCategory subCat = sCatRepo.findById(prodDto.getSubcategory()).orElse(null);
		prod.setSubcategory(subCat);
		prod.setBrand(prodDto.getBrand());
		prod.setProdName(prodDto.getProdName());
		prod.setDescription(prodDto.getDescription());
		prod.setCreatedAt(LocalDate.now());
		prod.setStock(prodDto.getStock());
		prod.setMrp(prodDto.getMrp());
		prod.setOfferPrice(prodDto.getOfferPrice());
		prod.setDiscount(prodDto.getDiscount());
		prod.setDiameter(prodDto.getDiameter() + "cm");
		prod.setThickness(prodDto.getThickness() + "mm");
		prod.setCapacity(prodDto.getCapacity() + "litre");
		prod.setWeight(prodDto.getWeight() + "kg");
		prod.setCartonDimension(prodDto.getCartonDimension());
		prod.setDimension(prodDto.getDimension());
		prod.setGuarantee(prodDto.getGuarantee() + "years");
		prod.setWarranty(prodDto.getWarranty() + "years");
		prod.setColor(prodDto.getColor());
		prod.setMaterial(prodDto.getMaterial());
		prod.setFinish(prodDto.getFinish());
		prepo.save(prod);
		if (!primaryImgURL.isEmpty()) {
			Images img = new Images();
			img.setpid(prod);
			MultipartFile file = primaryImgURL;
			String uploadDir = "public/ProductImages/";
			String fileName = prod.getPid() + "_primaryImage_" + file.getOriginalFilename();
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try (InputStream inputStream = file.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			img.setImageUrl(fileName);
			img.setIsPrimary(true);
			imgRepo.save(img);
		}
		if (secondaryImgURL != null) {
			for (MultipartFile file : secondaryImgURL) {
				if (!file.isEmpty()) {
					Images img = new Images();
					img.setpid(prod);
					String uploadDir = "public/ProductImages/";
					String fileName = prod.getPid() + "_secondaryImage_" + file.getOriginalFilename();
					Path uploadPath = Paths.get(uploadDir);
					if (!Files.exists(uploadPath)) {
						try {
							Files.createDirectories(uploadPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try (InputStream inputStream = file.getInputStream()) {
						Path filePath = uploadPath.resolve(fileName);
						Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						throw new IOException("Could not save uploaded file: " + fileName);
					}
					img.setImageUrl(fileName);
					img.setIsPrimary(false);
					imgRepo.save(img);
				}
			}
		}
		return "redirect:/admin/product";
	}

	@PostMapping("/addCoupon")
	public String addCoupon(@RequestParam String couponCode,
			@RequestParam String couponName, @RequestParam LocalDate validityDate,
			@RequestParam int discountRate, @RequestParam int uptoAmount, @RequestParam String couponDescription,
			@RequestParam int minCartValue, @RequestParam int minQuantity) {
		Coupon coupon = new Coupon();
		coupon.setCouponCode(couponCode);
		coupon.setCouponName(couponName);
		coupon.setExpireDate(validityDate);
		coupon.setDiscountRate(discountRate);
		coupon.setUptoAmount(uptoAmount);
		coupon.setMinCartValue(minCartValue);
		coupon.setMinQuantity(minQuantity);
		coupon.setIssueDate(LocalDate.now());
		coupon.setDescription(couponDescription);
		cRepo.save(coupon);
		return "redirect:/admin/coupon";
	}

	@PostMapping({ "/logout" })
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/changepassword")
	public String ChangePassword(HttpSession session, RedirectAttributes attrib, @RequestParam String oldpassword,
			@RequestParam String newpassword, @RequestParam String confirmpassword) {
		if (session.getAttribute("admin") != null) {
			Admin ad = adRepo.findByEmail((String) session.getAttribute("admin"));
			if (!newpassword.equals(confirmpassword)) {
				attrib.addFlashAttribute("message", "New password and confirmpassword are not same");
				return "redirect:/admin/setting";
			} else if (!oldpassword.equals(ad.getPassword())) {
				attrib.addFlashAttribute("message", "Old password is not correct");
				return "redirect:/admin/setting";
			} else {
				ad.setPassword(newpassword);
				adRepo.save(ad);
				return "redirect:/admin/setting";
			}
		} else {
			return "redirect:/admin/setting";
		}
	}

}
