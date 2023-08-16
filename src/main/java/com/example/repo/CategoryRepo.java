package com.example.repo;

import com.example.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepo extends JpaRepository<Category, String> {
    @Query("SELECT p FROM Category p")
    Page<Category> getAll(Pageable pageable);

}
