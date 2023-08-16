package com.example.controller;

import com.example.model.*;
import com.example.repo.*;
import com.example.service.CartService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    HttpSession session;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CartService cart;


    @ModelAttribute("cart")
    CartService getCart() {
        return cart;
    }

    @Data
    @AllArgsConstructor
    public static class PriceRange {
        int id;
        int minValue;
        int maxValue;
        String display;
    }

    List<AdminController.PriceRange> priceRangeList = Arrays.asList(
            new AdminController.PriceRange(0, 0, Integer.MAX_VALUE, "Tất cả"),
            new AdminController.PriceRange(1, 0, 10000000, "Dưới 10 triệu"),
            new AdminController.PriceRange(2, 10000000, 20000000, "Từ 10-20 triệu"),
            new AdminController.PriceRange(3, 20000000, Integer.MAX_VALUE, "Trên 20 triệu")
    );

    @RequestMapping("/home")
    public String index(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam("id") Optional<String> categoryId,
            @RequestParam(defaultValue = "0") int priceRangeId,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("priceRangeList", priceRangeList);
        model.addAttribute("categoryList", categoryService.getAll());
        int minPrice = priceRangeList.get(priceRangeId).minValue;
        int maxPrice = priceRangeList.get(priceRangeId).maxValue;

        Pageable pageable = PageRequest.of(page, 8);

        if (categoryId.isEmpty()) {
            Page<Product> data = productService.getByNamePrice("%" + keyword, minPrice, maxPrice, pageable);
            model.addAttribute("page", data);
        } else {
            Page<Product> data = productService.getByCategoryNamePrice(categoryId.get(), "%" + keyword, minPrice, maxPrice, pageable);
            model.addAttribute("page", data);

        }

        return "views/home/index";
    }

    @GetMapping("/detail/{id}")
    public String viewProduct(@PathVariable int id,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        Product product = productService.findByID(id);
        model.addAttribute("product", product);

        Pageable pageable = PageRequest.of(page, 4);
        Page<Product> data = productService.getByCategory(product.getCategory().getId(),pageable);
        model.addAttribute("page", data);

        return "views/home/detail";
    }

    @RequestMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable int id) {
        cart.add(id);
        return "redirect:/cart";
    }

    @RequestMapping("/remove-cart/{id}")
    public String removeCart(@PathVariable int id) {
        cart.remove(id);
        if (cart.getTotal() == 0) {
            return "redirect:/";
        }
        return "redirect:/cart";
    }

    @RequestMapping("/update-cart/{id}")
    public String updateCart(@PathVariable int id, int quantity) {
        cart.update(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart() {
        return "home/cart";
    }

    @GetMapping("/confirm")
    public String confirm() {
        return "home/confirm";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        return "views/home/about";
    }


//    @RequestMapping("/product/search-and-page")
//    public String searchAndPage(Model model,
//                                @RequestParam("keyword") Optional<String> kw,
//                                @RequestParam("p") Optional<Integer> p) {
//        String kwords = kw.orElse((String) session.getAttribute("keyword"));
//        session.setAttribute("keywords", kwords);
//        Pageable pageable = PageRequest.of(p.orElse(0), 5);
//        Page<Product> page = productRepo.getByName("%" + kwords + "%", pageable);
//        if (page.getContent().size() == 0) {
//            return "redirect:/product/search-and-page";
//        }
//        model.addAttribute("page", page);
//        return "product/search-page";
//    }
}
