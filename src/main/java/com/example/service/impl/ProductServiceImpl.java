package com.example.service.impl;

import com.example.model.Product;
import com.example.repo.ProductRepo;
import com.example.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductRepo productRepo;

    @Override
    public Page<Product> getProduct(Pageable pageable) {
        return productRepo.getAll(pageable);
    }

    @Override
    public Page<Product> getByCategoryNamePrice(String cid, String keyword, int minPrice, int maxPrice, Pageable pageable) {
        return productRepo.getByCategoryNamePrice(cid,keyword,minPrice,maxPrice,pageable);
    }

    @Override
    public Page<Product> getByNamePrice(String keyword, int minPrice, int maxPrice, Pageable pageable) {
        return productRepo.getByNamePrice(keyword,minPrice,maxPrice,pageable);
    }

    @Override
    public Page<Product> getByName(String keywords, Pageable pageable) {
        return productRepo.getByName(keywords,pageable);
    }

    @Override
    public Page<Product> getByCategory(String category, Pageable pageable) {
        return productRepo.getByCategory(category,pageable);
    }

    @Override
    public Product findByID(int id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void deleteById(int id) {
        productRepo.deleteById(id);
    }
}
