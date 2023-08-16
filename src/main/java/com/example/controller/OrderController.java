package com.example.controller;

import com.example.model.Account;
import com.example.model.Order;
import com.example.model.OrderDetail;
import com.example.repo.AccountRepo;
import com.example.repo.OrderDetailRepo;
import com.example.repo.OrderRepo;
import com.example.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    CartService cart;

    @Autowired
    HttpSession session;

    @Autowired
    private AccountRepo accountRepo;

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
}
