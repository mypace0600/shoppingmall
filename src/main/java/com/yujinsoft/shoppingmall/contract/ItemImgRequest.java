package com.yujinsoft.shoppingmall.contract;

import com.yujinsoft.shoppingmall.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImgRequest {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgRequest of(ItemImg itemImg){
        return modelMapper.map(itemImg,ItemImgRequest.class);
    }
}
