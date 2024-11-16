package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klpdapp.klpd.model.Attribute;

@Repository
public interface AttributeRepo extends JpaRepository <Attribute, String>{

}
