package com.yujinsoft.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @Column(name="order_item_id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private LocalDateTime regDt;

    private LocalDateTime updateDt;
}
