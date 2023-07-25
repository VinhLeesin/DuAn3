package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "email")
    String email;

    @Column(name = "admin")
    Boolean admin;

    @OneToMany(mappedBy = "account")
    List<Order> orders;

}
