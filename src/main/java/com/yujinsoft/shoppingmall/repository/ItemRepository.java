package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByItemNm(String itemNm);
}
