package com.yujinsoft.shoppingmall.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
}
