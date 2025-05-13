package com.klpdapp.klpd.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.dto.AddressDto;
import com.klpdapp.klpd.model.Address;
import com.klpdapp.klpd.model.Login;

@Service
public class AddressService {

    @Autowired
    AddressRepo addressrepo;

    public void addAddress(AddressDto aDto, Login user) {
        Address address = new Address();
        address.setUser(user);
        address.setNumber(aDto.getNumber());
        address.setAddress(aDto.getAddress());
        address.setName(aDto.getName());
        address.setPincode(aDto.getPincode());
        address.setCity(aDto.getCity());
        address.setState(aDto.getState());
        address.setCountry(aDto.getCountry());
        address.setLandmark(aDto.getLandmark());
        addressrepo.save(address);
    }

}
