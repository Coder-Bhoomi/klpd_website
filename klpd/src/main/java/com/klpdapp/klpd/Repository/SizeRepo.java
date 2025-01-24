package com.klpdapp.klpd.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Size;

public interface SizeRepo extends JpaRepository<Size, Integer>{

    @Query("SELECT DISTINCT s.size FROM Size s WHERE s.subcategory.id = :subcategoryId")
List<String> findDistinctSizesBySubcategorySubcategoryId(@Param("subcategoryId") String subcategoryId);

   

    
}
