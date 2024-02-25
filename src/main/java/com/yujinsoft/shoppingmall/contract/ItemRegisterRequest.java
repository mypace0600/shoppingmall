package com.yujinsoft.shoppingmall.contract;

import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRegisterRequest {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력값입니다.")
    private Integer price;

    @NotBlank(message = "상품 설명은 필수 입력값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgRequest> itemImgRequestList = new ArrayList<>();

    private List<Long> itemImgIdList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemRegisterRequest of(Item item){
        return modelMapper.map(item, ItemRegisterRequest.class);
    }
}
