package com.example.controller;

import com.example.model.Account;
import com.example.repo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    @Autowired
    private AccountRepo accountRepo;

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
}
