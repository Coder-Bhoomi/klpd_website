package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Attribute;
import com.klpdapp.klpd.model.Product;

public interface AttrRepo extends JpaRepository<Attribute, Integer> {
    List<Attribute> findByAttributeName(String name);

    List<Attribute> findByProduct(Product product);

}
