package com.yujinsoft.shoppingmall.repository;

import com.yujinsoft.shoppingmall.contract.ItemSearchRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchRequest itemSearchRequest, Pageable pageable);
}
