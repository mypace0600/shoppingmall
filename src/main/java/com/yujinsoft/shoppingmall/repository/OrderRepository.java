package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
