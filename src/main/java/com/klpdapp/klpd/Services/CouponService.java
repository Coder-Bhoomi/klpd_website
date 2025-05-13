package com.klpdapp.klpd.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.CouponRepo;
import com.klpdapp.klpd.model.Coupon;

@Service
public class CouponService {

    @Autowired
    CouponRepo couponRepo;

    public Optional<Coupon> getCouponByCode(String code) {
        return couponRepo.findByCouponCode(code);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }
}
