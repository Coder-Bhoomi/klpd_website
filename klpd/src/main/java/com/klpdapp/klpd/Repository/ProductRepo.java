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

       @Query("SELECT p FROM Product p WHERE (" +
                     "LOWER(CONCAT(COALESCE(p.capacity, ''), ' | ', COALESCE(p.diameter, ''))) LIKE LOWER(CONCAT('%', :size, '%')) "
                     +
                     "OR LOWER(CONCAT(COALESCE(p.diameter, ''), ' | ', COALESCE(p.capacity, ''))) LIKE LOWER(CONCAT('%', :size, '%')) "
                     +
                     "OR p.capacity = :size OR p.diameter = :size) " +
                     "AND p.subcategory.subcategoryId = :subcategoryId " +
                     "AND LOWER(p.prodName) LIKE '%induction%'")
       List<Product> findInductionProductsBySizeAndSubcategory(@Param("size") String size,
                     @Param("subcategoryId") String subcategoryId);

       @Query("SELECT p FROM Product p WHERE (" +
                     "LOWER(CONCAT(COALESCE(p.capacity, ''), ' | ', COALESCE(p.diameter, ''))) LIKE LOWER(CONCAT('%', :size, '%')) "
                     +
                     "OR LOWER(CONCAT(COALESCE(p.diameter, ''), ' | ', COALESCE(p.capacity, ''))) LIKE LOWER(CONCAT('%', :size, '%')) OR p.capacity = :size OR p.diameter = :size) " +
                     "AND p.subcategory.subcategoryId = :subcategoryId " +
                     "AND LOWER(p.prodName) NOT LIKE '%induction%'")
       List<Product> findNonInductionProductsBySizeAndSubcategory(@Param("size") String size,
                     @Param("subcategoryId") String subcategoryId);

       @Query(value = "SELECT DISTINCT CASE " +
                     "WHEN p.capacity IS NOT NULL AND p.diameter IS NOT NULL THEN CONCAT(p.capacity, ' | ', p.diameter) "
                     +
                     "WHEN p.capacity IS NOT NULL THEN p.capacity " +
                     "WHEN p.diameter IS NOT NULL THEN p.diameter " +
                     "END AS size " +
                     "FROM products p " +
                     "WHERE LOWER(p.prod_name) LIKE '%induction%' " +
                     "AND p.subcategory_id = :subcategoryId " +
                     "AND (p.capacity IS NOT NULL OR p.diameter IS NOT NULL) " +
                     "ORDER BY size", nativeQuery = true)
       List<String> findInductionSizesbySubcategory(@Param("subcategoryId") String subcategoryId);

       @Query(value = "SELECT DISTINCT CASE " +
                     "WHEN p.capacity IS NOT NULL AND p.diameter IS NOT NULL THEN CONCAT(p.capacity, ' | ', p.diameter) "
                     +
                     "WHEN p.capacity IS NOT NULL THEN p.capacity " +
                     "WHEN p.diameter IS NOT NULL THEN p.diameter " +
                     "END AS size " +
                     "FROM products p " +
                     "WHERE LOWER(p.prod_name) NOT LIKE '%induction%' " +
                     "AND p.subcategory_id = :subcategoryId " +
                     "AND (p.capacity IS NOT NULL OR p.diameter IS NOT NULL) " +
                     "ORDER BY size", nativeQuery = true)
       List<String> findNonInductionSizesbySubcategory(@Param("subcategoryId") String subcategoryId);

       Product findBySubcategory_SubcategoryId(String subcategoryId);

}
