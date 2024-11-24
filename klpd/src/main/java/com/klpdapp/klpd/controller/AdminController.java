package com.klpdapp.klpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.SizeRepo;
import com.klpdapp.klpd.Repository.SubCategoryRepo;
import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.Size;
import com.klpdapp.klpd.model.SubCategory;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({"/admin"})
public class AdminController {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CategoryRepo Catrepo;

	@Autowired
	private SubCategoryRepo sCatRepo;

	@Autowired
	SizeRepo srepo;

	 @GetMapping({"/dashboard"})
	 public String showIndex(Model model) {
	 return "admin/dashboard";
	 }

	@GetMapping({"/order"})
	public String showOrders(Model model) {
		List<Order> orders = orderRepository.findAll();
		model.addAttribute("Orders", orders);
		return "admin/order";
	}

	@GetMapping({"/category"})
	public String ShowCategories(Model model) {
		List<Category> categ = Catrepo.findAll();
		model.addAttribute("category", categ);
		return "admin/category";
	}

	@GetMapping({"/sub-category"})
	public String ShowSubCategory(Model model) {
		List<SubCategory> sCateg = sCatRepo.findAll();
		model.addAttribute("sub_category", sCateg);
		return "admin/sub_category";
	}
	@GetMapping({"/product"})
	public String ShowProduct(Model model) {
		
		return "admin/product";
	}
	@GetMapping({"/size"})
	public String ShowSize(Model model) {
		List<Size> s = srepo.findAll();
		model.addAttribute("size", s);
		return "admin/size";
	}
	@GetMapping({"/coupon"})
	public String ShowCoupon(Model model) {
		
		return "admin/coupon";
	}
	@GetMapping({"/setting"})
	public String ShowSetting(Model model) {
		
		return "admin/setting";
	}
}