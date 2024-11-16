package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Category;

public interface CategoryRepo extends JpaRepository <Category, String> {

}
