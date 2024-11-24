package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, String>{
    
}
