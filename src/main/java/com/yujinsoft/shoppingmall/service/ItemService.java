package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.ItemImg;
import com.yujinsoft.shoppingmall.repository.ItemImgRepository;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    public Long saveItem(ItemRegisterRequest itemRegisterRequest, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRegisterRequest.createItem();
        itemRepository.save(item);

        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }
            if(!"application/octet-stream".equals(itemImgFileList.get(i).getContentType())) {
                itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
            }
        }

        return item.getId();
    }
}
