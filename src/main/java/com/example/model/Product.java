package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    Integer price;

    @Column(name = "image")
    String image;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Createdate")
    String date;
    @ManyToOne
    @JoinColumn(name = "Categoryid")
    Category category;

    @OneToMany(mappedBy = "product")
    List<OrderDetail> orderDetails;

}
