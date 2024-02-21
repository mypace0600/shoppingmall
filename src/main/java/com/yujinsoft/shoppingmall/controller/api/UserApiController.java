package com.yujinsoft.shoppingmall.controller.api;

import com.yujinsoft.shoppingmall.common.ResponseDto;
import com.yujinsoft.shoppingmall.contract.UserRegister;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> register(@ModelAttribute("userRegister") UserRegister userRegister){
        log.debug("@@@@@@@@@@@@ userRegister :{}",userRegister.toString());
        User user = User.createUser(userRegister,passwordEncoder);
        userService.register(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
}
