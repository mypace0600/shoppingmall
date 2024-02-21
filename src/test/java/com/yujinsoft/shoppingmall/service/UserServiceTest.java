package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.UserRegister;
import com.yujinsoft.shoppingmall.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(){

        UserRegister userRegister = new UserRegister();
        userRegister.setUsername("test");
        userRegister.setPassword("1234");
        userRegister.setEmail("test@test.com");
        userRegister.setPhone("010-1234-5678");
        userRegister.setAddress("test address");

        return User.createUser(userRegister, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void registerTest() {
        User user = createUser();
        User savedUser = userService.register(user);
        assertEquals(user.getEmail(),savedUser.getEmail());
        assertEquals(user.getPassword(),savedUser.getPassword());
        assertEquals(user.getUsername(),savedUser.getUsername());
        assertEquals(user.getPhone(),savedUser.getPhone());
        assertEquals(user.getRole(),savedUser.getRole());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void duplicateRegisterTest(){
        User user1 = createUser();
        User user2 = createUser();
        userService.register(user1);
        Throwable e = assertThrows(IllegalStateException.class, ()-> {
            userService.register(user2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}