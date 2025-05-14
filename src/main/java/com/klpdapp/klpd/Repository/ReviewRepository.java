package com.klpdapp.klpd.Repository;

import com.klpdapp.klpd.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
