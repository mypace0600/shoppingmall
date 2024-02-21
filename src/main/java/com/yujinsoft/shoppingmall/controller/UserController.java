package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/auth/joinForm")
    public String joinForm(Model model)
    {
        model.addAttribute("userRegister",new UserRegister());
        return "user/joinForm";
    }
    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }
}
