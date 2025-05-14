package com.klpdapp.klpd.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, String>{

    Optional<SubCategory> findBySubcategoryName(String subcategory);

    List<SubCategory> findByCategoryCategoryId(String categoryId);

    
}
