package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByCategory_CategoryId(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpAsc(String categoryId);

    List<Product> findByCategory_CategoryIdOrderByMrpDesc(String categoryId);

    List<Product> findByProdNameContainingIgnoreCase(String query);

    List<Product> findTop4ByCategoryCategoryIdAndPidNot(String categoryId, int pid);
}
   
