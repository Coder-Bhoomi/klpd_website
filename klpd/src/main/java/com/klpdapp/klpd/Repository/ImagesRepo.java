package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Images;
import com.klpdapp.klpd.model.Product;


public interface ImagesRepo extends JpaRepository <Images, String> {

    @Query("SELECT i FROM Images i WHERE i.pid = :Product AND i.isPrimary = true")
    Images findPrimaryImageByProduct(@Param("Product") Product Product);

}
