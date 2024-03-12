package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>,
        ItemRepositoryCustom
{
    Item findByItemNm(String itemNm);

    Optional<Item> findById(Long itemId);
}
