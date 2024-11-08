package com.klpdapp.klpd.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klpdapp.klpd.model.product;

public interface productrepo extends JpaRepository<product, String> {

    List<product> findByCategory_CategoryId(String categoryId);

}
   
