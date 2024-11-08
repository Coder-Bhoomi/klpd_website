package com.klpdapp.klpd.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.product;

public interface productrepo extends JpaRepository<product, String> {

    List<product> findByCategory_CategoryId(String categoryId);

    List<product> findTop4ByCategoryCategoryIdAndProdIdNot(String categoryId, String prodId);

    List<product> findByCategory_CategoryIdOrderByMrpAsc(String categoryId);

    List<product> findByCategory_CategoryIdOrderByMrpDesc(String categoryId);

     @Query("SELECT DISTINCT p.attribute.color FROM product p WHERE p.category.categoryId = :categoryId")
    List<String> findDistinctColorsByCategory(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM product p WHERE p.category.categoryId = :categoryId AND p.attribute.color = :color")
    List<product> findByCategory_CategoryIdAndAttribute_Color(@Param("categoryId") String categoryId, @Param("color") String color);
}
   
