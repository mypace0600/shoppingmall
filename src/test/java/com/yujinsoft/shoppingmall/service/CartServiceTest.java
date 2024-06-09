package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.entity.CartItem;
import com.yujinsoft.shoppingmall.entity.CartItemDto;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import com.yujinsoft.shoppingmall.entity.enums.Role;
import com.yujinsoft.shoppingmall.repository.CartItemRepository;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import com.yujinsoft.shoppingmall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public User saveUser(){
        User user = new User();
        user.setEmail("hyunsu@test.com");
        user.setUsername("hyunsu");
        user.setAddress("address");
        user.setPassword("123");
        user.setPhone("123");
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    void addCart() {
        Item item = saveItem();
        User user = saveUser();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto,user.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(),cartItem.getCount());
    }
}