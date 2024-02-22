package com.yujinsoft.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    @GetMapping("/admin/item/register")
    public String itemForm(){
        return "/item/itemForm";
    }
}
