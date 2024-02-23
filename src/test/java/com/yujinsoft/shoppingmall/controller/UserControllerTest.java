package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.UserRegisterRequest;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(String email, String password){
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail(email);
        userRegisterRequest.setUsername("loginTest");
        userRegisterRequest.setPassword(password);
        userRegisterRequest.setPhone("010-0000-0000");
        userRegisterRequest.setAddress("test address");
        User user = User.createUser(userRegisterRequest, passwordEncoder);
        return userService.register(user);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@email.com";
        String password = "12341234";
        this.createUser(email,password);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/auth/loginProc")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "12341234";
        this.createUser(email,password);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/auth/loginProc")
                .user(email).password("123412345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}