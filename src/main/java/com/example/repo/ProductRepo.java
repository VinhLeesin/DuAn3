package com.example.repo;

import com.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.id=?1 AND p.name LIKE ?2 AND p.price BETWEEN ?3 AND ?4")
    Page<Product> searchByCategoryNamePrice(String cid, String keyword, int minPrice, int maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE ?1 AND p.price BETWEEN ?2 AND ?3")
    Page<Product> searchByNamePrice(String keyword, int minPrice, int maxPrice,  Pageable pageable);

    Page<Product> findAllByNameLike(String keywords, Pageable pageable);

}
