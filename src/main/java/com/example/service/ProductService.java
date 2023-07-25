package com.example.service;

import com.example.model.Product;
import com.example.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepo repo;
    public List<Product> getAll(){
        return repo.findAll();
    }

    public Product findById(int id) {
        return repo.findById(id).orElse(null);
    }
}
