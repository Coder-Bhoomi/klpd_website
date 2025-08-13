package com.klpdapp.klpd.Repository;

import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

   @Query("SELECT SUM(r.rating) FROM Review r WHERE r.product = :prod")
    Float getTotalRatingByProduct(Product prod);

    @Query("Select r from Review r where r.product = :prod AND r.comment IS NOT NULL AND r.comment <> ''")
    List<Review> findReviewWithText(Product prod);

    Integer countByProduct(Product prod);

    @Query("Select r.rating , count(r) from Review r where r.product = :prod group by r.rating")
    List<Object[]> getRatingDistributionByProduct(Product prod);
}
