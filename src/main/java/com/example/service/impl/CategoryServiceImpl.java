package com.example.service.impl;

import com.example.model.Category;
import com.example.repo.CategoryRepo;
import com.example.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryRepo.getAll(pageable);
    }

    @Override
    public Category getByID(String id) {
        return categoryRepo.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void deleteById(String id) {
        categoryRepo.deleteById(id);
    }
}
