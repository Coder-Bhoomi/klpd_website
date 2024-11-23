package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByCategory_CategoryId(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpAsc(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpDesc(String categoryId);

    List<Product> findByProdNameContainingIgnoreCase(String query);

    List<Product> findTop4ByCategoryCategoryIdAndPidNot(String categoryId, int pid);

    List<Product> findTop4ByOrderByCreatedAtDesc();

    List<Product> findTop4ByOrderByHitsDesc();

    @Query("SELECT DISTINCT p.capacity AS size FROM Product p WHERE p.subcategory.subcategoryId = :subcategoryId AND p.capacity IS NOT NULL " +
       "UNION SELECT DISTINCT p.thickness FROM Product p WHERE p.subcategory.subcategoryId = :subcategoryId AND p.thickness IS NOT NULL " +
       "UNION SELECT DISTINCT p.weight FROM Product p WHERE p.subcategory.subcategoryId = :subcategoryId AND p.weight IS NOT NULL " +
       "UNION SELECT DISTINCT p.diameter FROM Product p WHERE p.subcategory.subcategoryId = :subcategoryId AND p.diameter IS NOT NULL")
List<String> findDistinctSizesBySubcategoryId(@Param("subcategoryId") String subcategoryId);

@Query("SELECT p FROM Product p WHERE p.subcategory.subcategoryId = :subcategoryId AND " +
       "(p.capacity = :selectedSize OR p.thickness = :selectedSize OR p.weight = :selectedSize OR p.diameter = :selectedSize)")
Product findProductsBySizeAndSubcategory(
        @Param("subcategoryId") String subcategoryId,
        @Param("selectedSize") String selectedSize);

Product findBySubcategory_SubcategoryId(String subcategoryId);


}
   
