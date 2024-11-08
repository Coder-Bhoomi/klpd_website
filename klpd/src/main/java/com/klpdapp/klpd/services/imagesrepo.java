package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.images;
import com.klpdapp.klpd.model.product;


public interface imagesrepo extends JpaRepository <images, String> {

    @Query("SELECT i FROM images i WHERE i.prodId = :product AND i.isPrimary = true")
    images findPrimaryImageByProduct(@Param("product") product product);

}
