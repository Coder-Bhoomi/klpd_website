package com.klpdapp.klpd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.klpdapp.klpd.Repository.LoginRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.SubCategoryRepo;
import com.klpdapp.klpd.Repository.SegmentRepo;
import com.klpdapp.klpd.Repository.AttrRepo;
import com.klpdapp.klpd.dto.AttributeDto;
import com.klpdapp.klpd.dto.ProductDto;
import com.klpdapp.klpd.model.Admin;
import com.klpdapp.klpd.model.Attribute;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Coupon;
import com.klpdapp.klpd.model.Images;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.SubCategory;
import com.klpdapp.klpd.model.Segment;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasRole('ADMIN')")
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

	@Autowired
	SegmentRepo segmentRepo;

	@Autowired
	AttrRepo attRepo;

	@Autowired
	LoginRepo loginRepo;

	@GetMapping({ "/dashboard" })
	public String showIndex(Model model, HttpSession session) {
		Login user = loginRepo.findById((Integer) session.getAttribute("userid")).orElse(null);
		if (!user.getUserType().equals("Admin")) {
			return "redirect:/login";
		}
		else {
			List<Product> topsales = prepo.findTop4ByOrderBySalesDesc();
			model.addAttribute("topsales", topsales);
			List<Product> lessStock = prepo.findTop4ByOrderByStockAsc();
			model.addAttribute("lessStock", lessStock);
			return "admin/dashboard";
		}
	}

	@GetMapping("/user")
	public String ShowUsers(Model model) {
		List<Login> users = loginRepo.findAll();
		model.addAttribute("user", users);
		
		return "admin/user";
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
		List<Segment> seg = segmentRepo.findAll();
		model.addAttribute("segments", seg);
		return "admin/category";
	}

	@PostMapping("/addCategory")
	public String addCategory(@RequestParam("categoryName") String categoryName,
			@RequestParam("categoryId") String categoryId, @RequestParam("segmentName") int SegmentID,
			@RequestParam("categoryimage") MultipartFile CategoryImage) {
		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategoryName(categoryName);
		category.setSegment(segmentRepo.findById(SegmentID).orElse(null));
		MultipartFile file = CategoryImage;
		String uploadDir = "public/CategoryImages/";
		String originalFileName = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}
		String fileName = categoryName + fileExtension;
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
		category.setCategoryImage(fileName);
		Catrepo.save(category);
		return "redirect:/admin/category";
	}

	@PostMapping("/category/update")
	public String updateCategory(@RequestParam("categoryId") String categoryId,
			@RequestParam("categoryName") String categoryName,
			@RequestParam("segmentName") int SegmentID,
			@RequestParam(value = "newCategoryImage", required = false) MultipartFile newCategoryImage) {
		Category category = Catrepo.findById(categoryId).orElse(null);
		if (category == null) {
			return "redirect:/admin/category"; // Handle case where category doesn't exist
		}
		category.setCategoryName(categoryName);
		category.setSegment(segmentRepo.findById(SegmentID).orElse(null));
		System.out.println(categoryName);
		if (newCategoryImage != null && !newCategoryImage.isEmpty()) {
			String uploadDir = "public/CategoryImages/";
			String originalFileName = newCategoryImage.getOriginalFilename();
			String fileExtension = "";

			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String fileName = categoryName + fileExtension; // Naming file after category name
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (InputStream inputStream = newCategoryImage.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName);
			category.setCategoryImage(fileName); // Update image in the database
		}
		Catrepo.save(category);
		return "redirect:/admin/category";
	}

	@GetMapping({ "/segment" })
	public String ShowSegments(Model model) {
		List<Segment> seg = segmentRepo.findAll();
		model.addAttribute("segment", seg);
		return "admin/Segment";
	}

	@PostMapping("/segment/update")
	public String updateSegment(@RequestParam("segmentId") int segmentId,
			@RequestParam("segmentName") String segmentName,
			@RequestParam(value = "newSegmentImage", required = false) MultipartFile newSegmentImage) {

		// Fetch the segment from the database
		Segment segment = segmentRepo.findById(segmentId).orElse(null);
		if (segment == null) {
			return "redirect:/admin/segment"; // Handle case where segment doesn't exist
		}

		// Update segment name
		segment.setSegmentName(segmentName);
		System.out.println(segmentName);
		// If a new image is uploaded, update the image
		if (newSegmentImage != null && !newSegmentImage.isEmpty()) {
			String uploadDir = "public/SegmentImages/";
			String originalFileName = newSegmentImage.getOriginalFilename();
			String fileExtension = "";

			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String fileName = segmentName + fileExtension; // Naming file after segment name
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (InputStream inputStream = newSegmentImage.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName);
			segment.setSegmentImage(fileName); // Update image in the database
		}

		segmentRepo.save(segment);
		return "redirect:/admin/segment";
	}

	@PostMapping("/addsegment")
	public String addSegment(@RequestParam("segmentName") String SegmentName,
			@RequestParam("segmentImage") MultipartFile segmentImage) {
		Segment seg = new Segment();
		seg.setSegmentName(SegmentName);
		MultipartFile file = segmentImage;
		String uploadDir = "public/SegmentImages/";
		String originalFileName = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}
		String fileName = SegmentName + fileExtension;
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
		seg.setSegmentImage(fileName);
		segmentRepo.save(seg);
		return "redirect:/admin/segment";
	}

	@GetMapping({ "/sub-category" })
	public String ShowSubCategory(Model model) {
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);
		List<Category> categ = Catrepo.findAll();
		model.addAttribute("category", categ);
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
		List<String> attr = attRepo.findDistinctAttributeName();
		model.addAttribute("attributes", attr);
		return "admin/product";
	}

	@GetMapping({ "/productlist" })
	public String ShowProductList(Model model) {
		List<Product> p = prepo.findAll();
		model.addAttribute("product", p);
		return "admin/productlist";
	}

	@GetMapping("/{pid}")
	public String ShowProductDetail(Model model, @PathVariable Integer pid) {
		Product prod = prepo.findById(pid).orElse(null);
		model.addAttribute("product", prod);
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);
		List<Attribute> attr = attRepo.findByProduct(prod);
		model.addAttribute("attributes", attr);
		List<String> attribute = attRepo.findDistinctAttributeName();
		model.addAttribute("attribute", attribute);
		return "admin/productdetail";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product,
			@RequestParam(value = "primaryImage", required = false) MultipartFile primaryImage,
			@RequestParam(value = "secondaryImages", required = false) MultipartFile[] secondaryImages,
			@RequestParam(value = "removedImages", required = false) List<Integer> removedImages,
			RedirectAttributes redirectAttributes) {
		// Fetch the existing product from the database
		Product existingProduct = prepo.findById(product.getPid()).orElse(null);
		handleImageUploads(product, primaryImage, secondaryImages, removedImages);

		if (existingProduct != null) {
			// Update only necessary fields
			existingProduct.setProdName(product.getProdName());
			existingProduct.setDescription(product.getDescription());
			existingProduct.setBrand(product.getBrand());
			existingProduct.setHapPid(product.getHapPid());
			existingProduct.setCompanyPid(product.getCompanyPid());
			existingProduct.setMrp(product.getMrp());
			existingProduct.setDiscount(product.getDiscount());
			existingProduct.setStock(product.getStock());
			existingProduct.setSubcategory(product.getSubcategory());

			updateProductImages(existingProduct, product.getImages());

			// Update attributes
			updateProductAttributes(existingProduct, product.getAttributes());

			// Handle attributes
			existingProduct.getAttributes().clear(); // Remove old attributes
			for (Attribute attribute : product.getAttributes()) {
				attribute.setProduct(existingProduct); // Link to existing product
				existingProduct.getAttributes().add(attribute);
			}

			// Save the updated product
			prepo.save(existingProduct);
		}

		return "redirect:/admin/productdetail/" + product.getPid();
	}

	private void handleImageUploads(Product product, MultipartFile primaryImage,
			MultipartFile[] secondaryImages, List<Integer> removedImages) {
		List<Images> images = new ArrayList<>();

		// Handle primary image
		if (primaryImage != null && !primaryImage.isEmpty()) {
			String primaryImageUrl = product.getPid() + "_primaryImage_" + primaryImage.getOriginalFilename();
			String uploadDir = "public/ProductImages/";
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Images primaryImg = new Images();
			primaryImg.setImageUrl(primaryImageUrl);
			primaryImg.setIsPrimary(true);
			images.add(primaryImg);
		} else if (product.getImages() != null) {
			// Keep existing primary image if not changed
			product.getImages().stream()
					.filter(Images::getIsPrimary)
					.findFirst()
					.ifPresent(images::add);
		}

		// Handle secondary images
		if (secondaryImages != null) {
			for (MultipartFile file : secondaryImages) {
				if (!file.isEmpty()) {
					String imageUrl = product.getPid() + "_secondaryImage_" + file.getOriginalFilename();
					String uploadDir = "public/ProductImages/";
					Path uploadPath = Paths.get(uploadDir);
					if (!Files.exists(uploadPath)) {
						try {
							Files.createDirectories(uploadPath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Images img = new Images();
					img.setImageUrl(imageUrl);
					img.setIsPrimary(false);
					images.add(img);
				}
			}
		}

		// Add existing secondary images that weren't removed
		if (product.getImages() != null) {
			product.getImages().stream()
					.filter(img -> !img.getIsPrimary())
					.filter(img -> removedImages == null ||
							!removedImages.contains(img.getImgId()))
					.forEach(images::add);
		}

		product.setImages(images);
	}
	private void updateProductImages(Product product, List<Images> updatedImages) {
        if (updatedImages == null || updatedImages.isEmpty()) {
            return;
        }
        
        // Set product reference for all images
        updatedImages.forEach(img -> img.setpid(product));
        
        // Save all images (new ones will be inserted, existing ones updated)
        imgRepo.saveAll(updatedImages);
        
        // Delete images that are no longer referenced
        List<Integer> currentImageIds = updatedImages.stream()
                .map(Images::getImgId)
                .filter(id -> id != 0)
                .collect(Collectors.toList());
        
        if (!currentImageIds.isEmpty()) {
            //imgRepo.deleteByPidAndImgIdNotIn(product, currentImageIds);
        } else {
            imgRepo.deleteByPid(product);
        }
    }

	private void updateProductAttributes(Product product, List<Attribute> updatedAttributes) {
		if (updatedAttributes == null) {
			return;
		}

		// Remove empty attributes
		updatedAttributes
				.removeIf(attr -> (attr.getAttributeName() == null || attr.getAttributeName().trim().isEmpty()) &&
						(attr.getAttributeValue() == null || attr.getAttributeValue().trim().isEmpty()));

		// Fetch existing attributes from the database
		List<Attribute> existingAttributes = attRepo.findByProduct(product);

		Map<Integer, Attribute> existingAttributeMap = existingAttributes.stream()
				.collect(Collectors.toMap(Attribute::getAttribute_id, attr -> attr));

		// Process the updated attributes
		for (Attribute attr : updatedAttributes) {
			attr.setProduct(product);

			if (attr.getAttribute_id() == 0) {
				// New attribute, save it
				attRepo.save(attr);
			} else {
				// Check if it exists in the database
				Attribute existingAttr = existingAttributeMap.get(attr.getAttribute_id());
				if (existingAttr != null) {
					// Update existing attribute
					existingAttr.setAttributeName(attr.getAttributeName());
					existingAttr.setAttributeValue(attr.getAttributeValue());
					attRepo.save(existingAttr); // Save the updated attribute
				}
			}
		}

		// Collect IDs of updated attributes
		List<Integer> updatedAttributeIds = updatedAttributes.stream()
				.map(Attribute::getAttribute_id)
				.filter(id -> id != 0)
				.collect(Collectors.toList());

		// Remove attributes that are not in the updated list
		if (!updatedAttributeIds.isEmpty()) {
			attRepo.deleteByProductAndAttributeIdNotIn(product, updatedAttributeIds);
		} else {
			attRepo.deleteByProduct(product);
		}
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
		SubCategory subCat = sCatRepo.findById(prodDto.getSubcategory()).orElse(null);
		prod.setSubcategory(subCat);
		prod.setBrand(prodDto.getBrand());
		prod.setProdName(prodDto.getProdName());
		prod.setDescription(prodDto.getDescription());
		prod.setCreatedAt(LocalDate.now());
		prod.setStock(prodDto.getStock());
		prod.setMrp(prodDto.getMrp());
		prod.setDiscount(prodDto.getDiscount());
		prod.setOfferPrice(prodDto.getMrp() - (prodDto.getMrp() * prodDto.getDiscount() / 100));
		prod.setSales(0);
		prepo.save(prod);
		for (AttributeDto attributeDto : prodDto.getAttributes()) {
			Attribute attribute = new Attribute();
			attribute.setProduct(prod);
			attribute.setAttributeName(attributeDto.getAttributeName());
			System.out.println("Name:" + attributeDto.getAttributeName() + "Value:" + attributeDto.getAttributeValue());
			attribute.setAttributeValue(attributeDto.getAttributeValue());
			attRepo.save(attribute);
		}

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
			img.setImageUrl("/ProductImages/" + fileName);
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
					img.setImageUrl("/ProductImages/" + fileName);
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


