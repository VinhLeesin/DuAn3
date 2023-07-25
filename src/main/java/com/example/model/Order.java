package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "address")
    String address;

    @Temporal(TemporalType.DATE)
    @Column(name = "Createdate")
    Date createdate = new Date();

    @ManyToOne
    @JoinColumn(name = "Username")
    Account account;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;
}
