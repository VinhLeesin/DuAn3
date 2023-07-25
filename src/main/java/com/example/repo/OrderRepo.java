package com.example.repo;

import com.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.account.fullname LIKE ?1 AND o.createdate BETWEEN ?2 AND ?3")
    public List<Order> search(String user, Date d1, Date d2);
}
