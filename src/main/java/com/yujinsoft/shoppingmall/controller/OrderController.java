package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.config.PrincipalDetail;
import com.yujinsoft.shoppingmall.contract.OrderDto;
import com.yujinsoft.shoppingmall.contract.OrderHisDto;
import com.yujinsoft.shoppingmall.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, PrincipalDetail principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getUsername();
        Long orderId;
        try {
            orderId = orderService.order(orderDto,email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, PrincipalDetail principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);
        Page<OrderHisDto> orderHisList = orderService.getOrderList(principal.getUser().getEmail(),pageable);
        model.addAttribute("orders",orderHisList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    @ResponseBody
    public  ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, PrincipalDetail principal){
        if(!orderService.validateOrder(orderId,principal.getUser().getEmail())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

}
