package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Address;
import com.klpdapp.klpd.model.User;

public interface AddressRepo extends JpaRepository<Address, Integer>{

    List<Address> findByUser(User user);
}
