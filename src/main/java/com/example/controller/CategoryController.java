package com.example.controller;

import com.example.model.Category;
import com.example.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/admin/category/index")
    public String listCategory(@RequestParam(defaultValue = "0") int page,Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Category> items = categoryService.getAll(pageable);
        model.addAttribute("items", items);
        return "views/admin/category/list";
    }

    @PutMapping("/admin/category/update/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        Category category = categoryService.getByID(String.valueOf(id));
        model.addAttribute("category", category);
        return "views/admin/category/update";
    }

    @PostMapping("/admin/category/create")
    public String createOrUpdateCategory(Category item) {
        categoryService.save(item);
        return "redirect:/admin/category/index";
    }

    @GetMapping("/admin/category/viewAdd")
    public String viewAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "views/admin/category/add";
    }

    @DeleteMapping("/admin/category/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        categoryService.deleteById(id);
        return "redirect:/admin/category/index";
    }
}
