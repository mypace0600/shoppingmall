package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.OrderDto;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.Order;
import com.yujinsoft.shoppingmall.entity.OrderItem;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import com.yujinsoft.shoppingmall.repository.OrderRepository;
import com.yujinsoft.shoppingmall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto,String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }


}
