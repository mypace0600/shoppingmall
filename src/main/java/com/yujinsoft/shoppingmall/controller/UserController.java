package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.UserRegister;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/joinForm")
    public String joinForm(Model model)
    {
        model.addAttribute("userRegister",new UserRegister());
        return "user/joinForm";
    }

    @PostMapping("/auth/joinProc")
    public String register(@ModelAttribute("userRegister") UserRegister userRegister){
        User user = User.createUser(userRegister,passwordEncoder);
        userService.register(user);
        return "redirect:/";
    }


    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }
}
