package com.example.controller;

import com.example.model.*;
import com.example.repo.*;
import com.example.service.CartService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    HttpSession session;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    CartService cart;

    //  Category
    @GetMapping("/admin/category/index")
    public String listCategory(Model model) {
        List<Category> items = categoryRepo.findAll();
        model.addAttribute("items", items);
        return "views/admin/category/list";
    }

    @GetMapping("/admin/category/update/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        Category category = categoryRepo.findById(String.valueOf(id)).get();
        model.addAttribute("category", category);
        return "views/admin/category/update";
    }

    @PostMapping("/admin/category/create")
    public String createOrUpdateCategory(Category item) {
        categoryRepo.save(item);
        return "redirect:/admin/category/index";
    }

    @GetMapping("/admin/category/viewAdd")
    public String viewAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "views/admin/category/add";
    }

    @RequestMapping("/admin/category/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        categoryRepo.deleteById(id);
        return "redirect:/admin/category/index";
    }

    //  Product
    @GetMapping("/admin/product/index")
    public String listProduct(Model model) {
        List<Product> items = productRepo.findAll();
        model.addAttribute("items", items);
        return "views/admin/product/list";
    }

    @GetMapping("/admin/product/update/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("category", categories);
        Product product = productRepo.findById(id).get();
        model.addAttribute("product", product);
        return "views/admin/product/update";
    }

    @GetMapping("/admin/product/viewAdd")
    public String viewAddProduct(Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("category", categories);
        model.addAttribute("product", new Product());
        return "views/admin/product/add";
    }

    @PostMapping("/admin/product/create")
    public String createOrUpdateProduct(Product product) {
        productRepo.save(product);
        return "redirect:/admin/product/index";
    }

    @RequestMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productRepo.deleteById(id);
        return "redirect:/admin/product/index";
    }

    //  Account
    @GetMapping("/admin/account/index")
    public String listAccount(Model model) {
        List<Account> accounts = accountRepo.findAll();
        model.addAttribute("items", accounts);
        return "views/admin/account/list";
    }

    @PostMapping("/admin/account/create")
    public String createAccount(Account account) {
        accountRepo.save(account);
        return "redirect:/admin/account/index";
    }

    @GetMapping("/admin/account/update/{id}")
    public String editAccount(@PathVariable("id") String username, Model model) {
        Optional<Account> account = accountRepo.findById(username);
        model.addAttribute("account", account.orElse(null));
        return "views/admin/account/update";
    }

    @RequestMapping("/admin/account/viewAdd")
    public String viewAddAccount(Model model) {
        model.addAttribute("account", new Account());
        return "views/admin/account/add";
    }

    @RequestMapping("/admin/account/delete/{id}")
    public String deleteAccount(@PathVariable("id") String username) {
        accountRepo.deleteById(username);
        return "redirect:/admin/account/index";
    }

    @RequestMapping("/admin/order/index")
    public String listOrder(@RequestParam(defaultValue = "") String user,
                            @RequestParam(defaultValue = "01/01/1970") String fromDate,
                            @RequestParam(defaultValue = "31/12/2999") String toDate,
                            Model model) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1 = sdf.parse(fromDate + " 00:00:00");
        Date d2 = sdf.parse(toDate + " 23:59:59");
        List<Order> items = orderRepo.search("%" + user + "%", d1, d2);
        model.addAttribute("items", items);
        return "admin/order/index";
    }

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

        Pageable pageable = PageRequest.of(page, 6);
        if (categoryId.isEmpty()) {
            Page<Product> data = productRepo.searchByNamePrice("%" + keyword, minPrice, maxPrice, pageable);
            model.addAttribute("page", data);
        } else {
            Page<Product> data = productRepo.searchByCategoryNamePrice(categoryId.get(), "%" + keyword, minPrice, maxPrice, pageable);
            model.addAttribute("page", data);
        }

        return "views/home/index";
    }

    @GetMapping("/detail/{id}")
    public String viewProduct(@PathVariable int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
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

    @GetMapping("/login")
    public String login() {
        return "views/home/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Account acc = accountRepo.findByUsernameAndPassword(
                username, password).orElse(null);
        // TODO: Check DB
        if (acc != null) {
            session.setAttribute("username", username);
            return "redirect:/home";
        } else {
            model.addAttribute("message", "Tên đăng nhập/mật khẩu không đúng");
            return "views/home/login";
        }
    }

    @PostMapping("/purchase")
    public String purchase(@RequestParam String address) {
        System.out.println("address=" + address);
        System.out.println("items=" + cart.getItems());

        String un = (String) session.getAttribute("username");
        Account acc = accountRepo.findById(un).orElse(null);
        if (acc != null) {
            Order order = new Order();
            order.setAddress(address);
            order.setAccount(acc);
            orderRepo.save(order);
            for (OrderDetail item : cart.getItems()) {
                item.setOrder(order);
                orderDetailRepo.save(item);
            }
        }
        cart.clear();
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.removeAttribute("username");
        return "redirect:/login";
    }

    @RequestMapping("/product/search-and-page")
    public String searchAndPage(Model model,
                                @RequestParam("keyword") Optional<String> kw,
                                @RequestParam("p") Optional<Integer> p) {
        String kwords = kw.orElse((String) session.getAttribute("keyword"));
        session.setAttribute("keywords", kwords);
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Product> page = productRepo.findAllByNameLike("%" + kwords + "%", pageable);
        if (page.getContent().size() == 0) {
            return "redirect:/product/search-and-page";
        }
        model.addAttribute("page", page);
        return "product/search-page";
    }
}
