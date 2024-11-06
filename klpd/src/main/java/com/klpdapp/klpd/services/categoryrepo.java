package com.klpdapp.klpd.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.category;

public interface categoryrepo extends JpaRepository <category, String> {

}
