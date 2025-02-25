package com.klpdapp.klpd.Services;

import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.model.Product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepository;

    @Transactional
    public void incrementProductHits(int pid) {
        productRepository.incrementHits(pid);
    }

    @Transactional
    public void incrementSales(int pid) {
        productRepository.incrementSales(pid);
    }

    public Product findById(int pid) {
       Product product = productRepository.findById(pid).orElse(null);
       return product;
    }
    
}
