package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.ItemImgRequest;
import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.ItemImg;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import com.yujinsoft.shoppingmall.repository.ItemImgRepository;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for(int i=0;i<5;i++){
            String path = "/Users/hyunsu/Desktop";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin",roles = "ADMIN")
    void saveItemTest() throws Exception {
        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest();
        itemRegisterRequest.setItemNm("test");
        itemRegisterRequest.setItemSellStatus(ItemSellStatus.SELL);
        itemRegisterRequest.setItemDetail("test item");
        itemRegisterRequest.setPrice(10000);
        itemRegisterRequest.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemRegisterRequest,multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemRegisterRequest.getItemNm(), item.getItemNm());
        assertEquals(itemRegisterRequest.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemRegisterRequest.getItemDetail(), item.getItemDetail());
        assertEquals(itemRegisterRequest.getPrice(), item.getPrice());
        assertEquals(itemRegisterRequest.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }

    @Test
    @DisplayName("상품 조회 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getItemDetailTest() throws Exception {

        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest();
        itemRegisterRequest.setItemNm("test");
        itemRegisterRequest.setItemSellStatus(ItemSellStatus.SELL);
        itemRegisterRequest.setItemDetail("test item");
        itemRegisterRequest.setPrice(10000);
        itemRegisterRequest.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemRegisterRequest,multipartFileList);

        ItemRegisterRequest savedItemDetail = itemService.getItemDetail(itemId);
        assertEquals(savedItemDetail.getItemNm(),itemRegisterRequest.getItemNm());
        assertEquals(savedItemDetail.getItemImgIdList(),itemRegisterRequest.getItemImgIdList());
    }

    @Test
    @DisplayName("상품 수정 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateItemTest() throws Exception {
        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest();
        itemRegisterRequest.setItemNm("test");
        itemRegisterRequest.setItemSellStatus(ItemSellStatus.SELL);
        itemRegisterRequest.setItemDetail("test item");
        itemRegisterRequest.setPrice(10000);
        itemRegisterRequest.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemRegisterRequest,multipartFileList);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgRequest> itemImgRequestList = new ArrayList<>();
        List<Long> itemImgIdList = new ArrayList<>();
        for(ItemImg i : itemImgList){
            ItemImgRequest itemImgRequest = ItemImgRequest.of(i);
            itemImgRequestList.add(itemImgRequest);
            itemImgIdList.add(i.getId());
        }
        ItemRegisterRequest savedItem = itemService.getItemDetail(itemId);
        savedItem.setItemImgRequestList(itemImgRequestList);
        savedItem.setItemImgIdList(itemImgIdList);
        savedItem.setItemNm("test edit");


        List<MultipartFile> updatedMultipartFileList = createMultipartFiles();
        Long updatedItemId = itemService.updateItem(savedItem,updatedMultipartFileList);

        Item updatedItem = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(updatedItem.getItemNm(), savedItem.getItemNm());
    }


}