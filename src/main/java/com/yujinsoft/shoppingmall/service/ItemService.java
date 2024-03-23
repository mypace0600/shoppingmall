package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.ItemImgRequest;
import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.contract.ItemSearchRequest;
import com.yujinsoft.shoppingmall.contract.MainItemRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.ItemImg;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    public ItemRegisterRequest getItemDetail(Long itemId){

        List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);
        List<ItemImgRequest> itemImgRequestList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){
            ItemImgRequest itemImgRequest = ItemImgRequest.of(itemImg);
            itemImgRequestList.add(itemImgRequest);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemRegisterRequest itemRegisterRequest = ItemRegisterRequest.of(item);
        itemRegisterRequest.setItemImgRequestList(itemImgRequestList);

        return itemRegisterRequest;
    }

    public Long updateItem(ItemRegisterRequest itemRegisterRequest, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemRegisterRequest.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemRegisterRequest);
        if(!itemImgFileList.get(0).isEmpty()) {
            itemImgService.updateItemImg(itemRegisterRequest.getId(), itemImgFileList);
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItems(ItemSearchRequest itemSearchRequest, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchRequest,pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemRequest> getMainItemPage(ItemSearchRequest itemSearchRequest, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchRequest,pageable);
    }
}
