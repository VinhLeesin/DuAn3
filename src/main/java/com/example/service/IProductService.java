package com.example.service;

import com.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<Product> getProduct(Pageable pageable);

    Page<Product> getByCategoryNamePrice(String cid, String keyword, int minPrice, int maxPrice, Pageable pageable);

    Page<Product> getByNamePrice(String keyword, int minPrice, int maxPrice,  Pageable pageable);

    Page<Product> getByName(String keywords, Pageable pageable);

    Page<Product> getByCategory(String category, Pageable pageable);

    Product findByID(int id);

    Product save(Product product);

    void deleteById(int id);
}
