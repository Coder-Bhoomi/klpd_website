package com.klpdapp.klpd.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.klpdapp.klpd.Repository.AdminRepo;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.CouponRepo;
import com.klpdapp.klpd.Repository.ImagesRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.SizeRepo;
import com.klpdapp.klpd.Repository.SubCategoryRepo;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Size;
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
	SizeRepo srepo;

	@Autowired
	ImagesRepo imgRepo;

	@Autowired
	AdminRepo adrepo;

	@Autowired
	CouponRepo cRepo;

	@GetMapping({ "/dashboard" })
	public String showIndex(Model model) {
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

	@GetMapping({ "/product-image" })
	public String ShowProdImage(Model model) {

		return "admin/product_image";
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
		return "admin/product";
	}

	@GetMapping({ "/size" })
	public String ShowSize(Model model) {
		List<Size> s = srepo.findAll();
		model.addAttribute("size", s);
		return "admin/size";
	}
	

	@PostMapping("/size/update")
	public String updateSize(@RequestParam String subcategoryID, @ModelAttribute Size size) {
		SubCategory subCategory = sCatRepo.findById(subcategoryID).orElse(null);
		size.setSubcategory(subCategory);
		System.out.println(size.getSubcategory());
		srepo.save(size);

		return "redirect:/admin/size";
	}

	@GetMapping({ "/productlist" })
	public String ShowProductList(Model model) {
		List<Product> p = prepo.findAll();
		model.addAttribute("product", p);
		return "admin/productlist";
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

	// product entry
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
			@RequestParam("scCategoryId") Category scCategoryId,
			@RequestParam("SubCategoryName") String SubCategoryName) {
		SubCategory subCat = new SubCategory();
		subCat.setSubcategoryId(SubCategoryId);
		subCat.setSubcategoryName(SubCategoryName);
		subCat.setCategory(scCategoryId);
		sCatRepo.save(subCat);
		return "redirect:/admin/product";
	}

	@PostMapping("/addSize")
	public String addSize(@RequestParam("size") String size,
			@RequestParam("sizeSubCategoryId") SubCategory sizeSubCategoryId) {
		Size siz = new Size();
		siz.setUnit(size);
		siz.setSubcategory(sizeSubCategoryId);
		srepo.save(siz);
		return "redirect:/admin/product";
	}

	@PostMapping("/addNewProduct")
	public String addNewProduct(@RequestParam("id") String id, @RequestParam("productId") int productId,
			@RequestParam("hapId") String hapId,
			@RequestParam("pcategoryId") Category pcategoryId,
			@RequestParam("psubCategoryId") SubCategory psubCategoryId, @RequestParam("brand") String brand,
			@RequestParam("productName") String productName, @RequestParam("createdAt") LocalDate createdAt,
			@RequestParam("stock") int stock, @RequestParam("price") float price,
			@RequestParam("percentage") int percentage,
			@RequestParam("offeredPrice") float offeredPrice, @RequestParam("diameter") String diameter,
			@RequestParam("thickness") String thickness, @RequestParam("capacity") String capacity,
			@RequestParam("weight") String weight, @RequestParam("cartonDimension") String cartonDimension,
			@RequestParam("guarantee") String guarantee, @RequestParam("warranty") String warranty,
			@RequestParam("color") String color, @RequestParam("material") String material,
			@RequestParam("finish") String finish, @RequestParam("description") String description) {
		Product prod = new Product();
		prod.setPid(productId);
		prod.setCompanyPid(id);
		prod.setHapPid(hapId);
		prod.setCategory(pcategoryId);
		prod.setSubcategory(psubCategoryId);
		prod.setBrand(brand);
		prod.setProdName(productName);
		prod.setCreatedAt(createdAt);
		prod.setStock(stock);
		prod.setMrp(price);
		prod.setPercentage(percentage);
		prod.setOfferPrice(offeredPrice);
		prod.setDiameter(diameter);
		prod.setThickness(thickness);
		prod.setCapacity(capacity);
		prod.setWeight(weight);
		prod.setCartonDimension(cartonDimension);
		prod.setGuarantee(guarantee);
		prod.setWarranty(warranty);
		prod.setColor(color);
		prod.setMaterial(material);
		prod.setFinish(finish);
		prod.setDescription(description);
		prepo.save(prod);
		return "redirect:/admin/product";
	}

	@PostMapping("/addCoupon")
	public String addCoupon(@RequestParam int couponId, @RequestParam String couponCode,
			@RequestParam String couponName, @RequestParam LocalDate validityDate,
			@RequestParam int discountRate, @RequestParam int uptoAmount, @RequestParam String couponDescription) {
		Coupon coupon = new Coupon();
		coupon.setCouponId(couponId);
		coupon.setCouponCode(couponCode);
		coupon.setCouponName(couponName);
		coupon.setIssueDate(validityDate);
		coupon.setDiscountRate(discountRate);
		coupon.setUptoAmount(uptoAmount);
		coupon.setDescription(couponDescription);
		cRepo.save(coupon);
		return "redirect:/admin/product";
	}

	@PostMapping({ "/logout" })
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
