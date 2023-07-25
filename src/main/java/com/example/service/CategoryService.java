package com.example.service;

import com.example.model.Category;
import com.example.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo repo;

    public List<Category> getAll() {
        return repo.findAll();
    }
}
