package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Product;

public interface ProductRepo extends JpaRepository<Product, String> {

    List<Product> findByCategory_CategoryId(String categoryId);

    List<Product> findTop4ByCategoryCategoryIdAndProdIdNot(String categoryId, String prodId);

    List<Product> findByCategory_CategoryIdOrderByMrpAsc(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpDesc(String categoryId);

     @Query("SELECT DISTINCT p.attribute.color FROM Product p WHERE p.category.categoryId = :categoryId")
    List<String> findDistinctColorsByCategory(@Param("categoryId") String categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND p.attribute.color = :color")
    List<Product> findByCategory_CategoryIdAndAttribute_Color(@Param("categoryId") String categoryId, @Param("color") String color);

    List<Product> findByProdNameContainingIgnoreCase(String query);

    List<Product> findByProdNameContainingIgnoreCaseAndAttribute_Color(String query, String color);
}
   
