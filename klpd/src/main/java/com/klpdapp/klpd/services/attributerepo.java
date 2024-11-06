package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klpdapp.klpd.model.attribute;

@Repository
public interface attributerepo extends JpaRepository <attribute, String>{

}
