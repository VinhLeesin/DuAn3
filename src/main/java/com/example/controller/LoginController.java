package com.example.controller;

import com.example.model.Account;
import com.example.repo.AccountRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    HttpSession session;

    @Autowired
    private AccountRepo accountRepo;
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

    @GetMapping("/logout")
    public String logout() {
        session.removeAttribute("username");
        return "redirect:/login";
    }
}
