package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByCategory_CategoryId(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpAsc(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpDesc(String categoryId);

    List<Product> findTop4ByOrderByCreatedAtDesc();

    List<Product> findTop4ByOrderByHitsDesc();

    @Modifying
    @Query("UPDATE Product p SET p.hits = p.hits + 1 WHERE p.pid = :pid")
    void incrementHits(@Param("pid") Integer pid);

    @Modifying
    @Query("Update Product p Set p.sales=p.sales + 1 where p.pid = :pid")
    void incrementSales(int pid);
    @Query("SELECT p FROM Product p WHERE " +
    "EXISTS (SELECT 1 FROM Attribute a WHERE a.product = p " +
    "AND a.attributeName IN ('capacity', 'diameter') " +
    "AND (:size LIKE CONCAT('%', a.attributeValue, '%'))) " +
    "AND p.subcategory.subcategoryId = :subcategoryId " +
    "AND LOWER(p.prodName) LIKE '%induction%'")
List<Product> findInductionProductsBySizeAndSubcategory(
    @Param("size") String size, 
    @Param("subcategoryId") String subcategoryId);


    @Query("SELECT p FROM Product p WHERE (" +
            "EXISTS (SELECT 1 FROM  Attribute a WHERE a.product = p " +
            "AND a.attributeName IN ('capacity', 'diameter') " +
            "AND  (:size LIKE CONCAT('%', a.attributeValue, '%'))) " +
            ") " +
            "AND p.subcategory.subcategoryId = :subcategoryId " +
            "AND LOWER(p.prodName) NOT LIKE '%induction%'")
    List<Product> findNonInductionProductsBySizeAndSubcategory(@Param("size") String size,
            @Param("subcategoryId") String subcategoryId);

    @Query(value = "SELECT DISTINCT " +
            "CASE " +
            "WHEN ca1.attribute_value IS NOT NULL AND ca2.attribute_value IS NOT NULL " +
            "THEN CONCAT(ca1.attribute_value, ' | ', ca2.attribute_value) " +
            "WHEN ca1.attribute_value IS NOT NULL THEN ca1.attribute_value " +
            "WHEN ca2.attribute_value IS NOT NULL THEN ca2.attribute_value " +
            "END AS size " +
            "FROM products p " +
            "LEFT JOIN attribute_table ca1 ON p.pid = ca1.pid AND ca1.attribute_name = 'capacity' " +
            "LEFT JOIN attribute_table ca2 ON p.pid = ca2.pid AND ca2.attribute_name = 'diameter' " +
            "WHERE LOWER(p.prod_name) LIKE '%induction%' " +
            "AND p.subcategory_id = :subcategoryId " +
            "AND (ca1.attribute_value IS NOT NULL OR ca2.attribute_value IS NOT NULL) " +
            "ORDER BY size", nativeQuery = true)
    List<String> findInductionSizesbySubcategory(@Param("subcategoryId") String subcategoryId);

    @Query(value = "SELECT DISTINCT " +
            "CASE " +
            "WHEN ca1.attribute_value IS NOT NULL AND ca2.attribute_value IS NOT NULL " +
            "THEN CONCAT(ca1.attribute_value, ' | ', ca2.attribute_value) " +
            "WHEN ca1.attribute_value IS NOT NULL THEN ca1.attribute_value " +
            "WHEN ca2.attribute_value IS NOT NULL THEN ca2.attribute_value " +
            "END AS size " +
            "FROM products p " +
            "LEFT JOIN attribute_table ca1 ON p.pid = ca1.pid AND ca1.attribute_name = 'capacity' " +
            "LEFT JOIN attribute_table ca2 ON p.pid = ca2.pid AND ca2.attribute_name = 'diameter' " +
            "WHERE LOWER(p.prod_name) NOT LIKE '%induction%' " +
            "AND p.subcategory_id = :subcategoryId " +
            "AND (ca1.attribute_value IS NOT NULL OR ca2.attribute_value IS NOT NULL) " +
            "ORDER BY size", nativeQuery = true)
    List<String> findNonInductionSizesbySubcategory(@Param("subcategoryId") String subcategoryId);

    Product findBySubcategory_SubcategoryId(String subcategoryId);

    List<Product> findTop4BySubcategorySubcategoryIdAndPidNot(String subcategoryId, int pid);

    List<Product> findTop4ByOrderBySalesDesc();

    List<Product> findTop4ByOrderByStockAsc();

}
