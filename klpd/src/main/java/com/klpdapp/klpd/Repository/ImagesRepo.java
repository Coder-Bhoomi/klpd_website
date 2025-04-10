package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.klpdapp.klpd.model.Images;
import com.klpdapp.klpd.model.Product;


public interface ImagesRepo extends JpaRepository <Images, Integer> {

    @Query("SELECT i FROM Images i WHERE i.pid = :Product AND i.isPrimary = true")
    Images findPrimaryImageByProduct(@Param("Product") Product Product);

    @Modifying
    @Transactional
    @Query("DELETE FROM Images i WHERE i.pid = :Product")
    void deleteByPid(@Param("Product") Product Product);

    @Modifying
    @Transactional
    @Query("DELETE FROM Images i WHERE i.pid = :Product AND i.isPrimary = true")
    void deletePrimaryImageByProduct(@Param("Product") Product Product);

  /*   @Modifying
    @Transactional
    @Query("DELETE FROM Images i WHERE i.pid = :Product AND i.imgId NOT IN :imgIds")
    void deleteByPidAndImgIdNotIn(@Param("Product") Product Product,@Param("imgIds") List<Integer> imgIds); */
}
