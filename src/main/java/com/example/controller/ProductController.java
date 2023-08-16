package com.example.controller;

import com.example.model.Category;
import com.example.model.Product;
import com.example.service.impl.CategoryServiceImpl;
import com.example.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/admin/product/index")
    public String listProduct(@RequestParam(defaultValue = "0") int page,Model model) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<Product> items = productService.getProduct(pageable);
        model.addAttribute("items", items);
        return "views/admin/product/list";
    }

    @PutMapping("/admin/product/update/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("category", categories);
        Product product = productService.findByID(id);
        model.addAttribute("product", product);
        return "views/admin/product/update";
    }

    @GetMapping("/admin/product/viewAdd")
    public String viewAddProduct(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("category", categories);
        model.addAttribute("product", new Product());
        return "views/admin/product/add";
    }

    @PostMapping("/admin/product/create")
    public String createOrUpdateProduct(Product product) {
        productService.save(product);
        return "redirect:/admin/product/index";
    }

    @DeleteMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteById(id);
        return "redirect:/admin/product/index";
    }
}
