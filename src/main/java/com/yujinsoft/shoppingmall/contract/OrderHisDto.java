package com.yujinsoft.shoppingmall.contract;

import com.yujinsoft.shoppingmall.entity.Order;
import com.yujinsoft.shoppingmall.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHisDto {

    public OrderHisDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
