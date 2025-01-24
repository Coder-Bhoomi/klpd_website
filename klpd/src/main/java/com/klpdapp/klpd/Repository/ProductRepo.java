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

    @Query("SELECT p FROM Product p WHERE (p.capacity = :size OR p.thickness = :size OR p.diameter = :size) AND p.subcategory.subcategoryId = :subcategoryId")
    Product findProductBySizeAndSubcategory(@Param("size") String size, @Param("subcategoryId") String subcategoryId);
    
    Product findBySubcategory_SubcategoryId(String subcategoryId);


}
   
