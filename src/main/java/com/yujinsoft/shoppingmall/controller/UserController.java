package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.UserRegisterRequest;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("userRegister",new UserRegisterRequest());
        return "user/joinForm";
    }

    @PostMapping("/auth/joinProc")
    public String register(@Valid @ModelAttribute("userRegister") UserRegisterRequest userRegisterRequest, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "auth/joinForm";
        }
        try {
            User user = User.createUser(userRegisterRequest, passwordEncoder);
            userService.register(user);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "auth/joinForm";
        }
        return "redirect:/";
    }


    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }

    @GetMapping("/auth/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "user/loginForm";
    }
}
