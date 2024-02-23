package com.yujinsoft.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    private int count;
}
