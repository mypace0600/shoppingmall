package com.yujinsoft.shoppingmall.entity;

import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(nullable = false)
    private Integer price;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Column(nullable = false)
    private Integer stockNumber;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemRegisterRequest itemRegisterRequest){
        this.itemNm = itemRegisterRequest.getItemNm();
        this.price = itemRegisterRequest.getPrice();
        this.itemDetail = itemRegisterRequest.getItemDetail();
        this.stockNumber = itemRegisterRequest.getStockNumber();
        this.itemSellStatus = itemRegisterRequest.getItemSellStatus();
    }
}
