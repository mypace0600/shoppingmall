package com.yujinsoft.shoppingmall.contract;

import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchRequest {

    private String searchDateType;

    private ItemSellStatus itemSellStatus;

    private String searchBy;

    private String searchQuery;
}
