package com.example.service;

import com.example.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategory();
    Page<Category> getAll(Pageable pageable);

    Category getByID(String id);

    Category save(Category category) ;

    void deleteById(String id);
}
