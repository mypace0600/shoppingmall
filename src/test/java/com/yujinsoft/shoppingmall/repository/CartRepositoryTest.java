package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.contract.UserRegisterRequest;
import com.yujinsoft.shoppingmall.entity.Cart;
import com.yujinsoft.shoppingmall.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public User createUserForTest(){
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test@test.com");
        userRegisterRequest.setUsername("test");
        userRegisterRequest.setPhone("010-0000-0000");
        userRegisterRequest.setPassword("12341234");
        userRegisterRequest.setAddress("test address");
        return User.createUser(userRegisterRequest,passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndUserTest(){
        User user = createUserForTest();
        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);


        assertEquals(savedCart.getUser().getId(), user.getId());
    }
}