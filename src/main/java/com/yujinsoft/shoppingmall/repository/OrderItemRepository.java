package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
