package com.klpdapp.klpd.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Coupon;

public interface CouponRepo extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findByCouponCode(String code);

}
