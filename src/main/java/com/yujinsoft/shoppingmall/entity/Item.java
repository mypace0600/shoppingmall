package com.yujinsoft.shoppingmall.entity;

import com.yujinsoft.shoppingmall.common.OutOfStockException;
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

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : "+this.stockNumber+")");
        }
        this.stockNumber = restStock;
    }
}
