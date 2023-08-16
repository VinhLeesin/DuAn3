package com.example.repo;

import com.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.id=?1 AND p.name LIKE ?2 AND p.price BETWEEN ?3 AND ?4")
    Page<Product> getByCategoryNamePrice(String cid, String keyword, int minPrice, int maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE ?1 AND p.price BETWEEN ?2 AND ?3")
    Page<Product> getByNamePrice(String keyword, int minPrice, int maxPrice,  Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE ?1")
    Page<Product> getByName(String keywords, Pageable pageable);

    @Query("SELECT p FROM Product p")
    Page<Product> getAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id=?1")
    Page<Product> getByCategory(String cid, Pageable pageable);
}
