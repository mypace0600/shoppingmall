package com.yujinsoft.shoppingmall.entity;

import com.yujinsoft.shoppingmall.contract.UserRegisterRequest;
import com.yujinsoft.shoppingmall.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "test", roles = "USER")
    public void auditingTest(){

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUsername("test");
        userRegisterRequest.setEmail("test@test.com");
        userRegisterRequest.setPhone("010-0000-0000");
        userRegisterRequest.setPassword("12341234");
        userRegisterRequest.setAddress("test address");

        User user = User.createUser(userRegisterRequest,passwordEncoder);
        userRepository.save(user);

        em.flush();
        em.clear();

        User oldUser = userRepository.findById(user.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + oldUser.getRegTime());
        System.out.println("update time : " + oldUser.getUpdateTime());
        System.out.println("create user : " + oldUser.getCreatedBy());
        System.out.println("modify user : " + oldUser.getModifiedBy());
    }
}