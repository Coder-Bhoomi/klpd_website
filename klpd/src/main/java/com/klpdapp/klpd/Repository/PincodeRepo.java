package com.klpdapp.klpd.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Pincode;

public interface PincodeRepo extends JpaRepository<Pincode, Integer> {

    @Query(value = "SELECT * FROM pincode WHERE pincode = :pincode LIMIT 1", nativeQuery = true)
    Optional<Pincode> findByPincode(@Param("pincode") Integer pincode);
    
}
