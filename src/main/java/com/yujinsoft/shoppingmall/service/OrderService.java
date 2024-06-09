package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.OrderDto;
import com.yujinsoft.shoppingmall.contract.OrderHisDto;
import com.yujinsoft.shoppingmall.contract.OrderItemDto;
import com.yujinsoft.shoppingmall.entity.*;
import com.yujinsoft.shoppingmall.repository.ItemImgRepository;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import com.yujinsoft.shoppingmall.repository.OrderRepository;
import com.yujinsoft.shoppingmall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;


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

    @Transactional(readOnly = true)
    public Page<OrderHisDto> getOrderList(String email, Pageable pageable){
        List<Order> orderList = orderRepository.findOrderList(email,pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHisDto> orderHisDtoList = new ArrayList<>();

        for(Order order : orderList){
            OrderHisDto orderHisDto = new OrderHisDto(order);
            List<OrderItem> orderItemList = order.getOrderItems();
            for(OrderItem orderItem : orderItemList){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(),"Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem,itemImg.getImgUrl());
                orderHisDto.addOrderItemDto(orderItemDto);
            }
            orderHisDtoList.add(orderHisDto);
        }
        return new PageImpl<OrderHisDto>(orderHisDtoList,pageable,totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        User currentUser = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        User savedUser = order.getUser();

        if(!StringUtils.equals(currentUser.getEmail(),savedUser.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
}
