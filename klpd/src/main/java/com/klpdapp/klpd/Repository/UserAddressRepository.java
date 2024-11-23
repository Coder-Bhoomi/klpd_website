package com.klpdapp.klpd.Repository;

import com.klpdapp.klpd.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<Address, Integer> {
}
 
