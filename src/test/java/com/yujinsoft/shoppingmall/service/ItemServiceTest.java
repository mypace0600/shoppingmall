package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.contract.ItemImgRequest;
import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.ItemImg;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import com.yujinsoft.shoppingmall.repository.ItemImgRepository;
import com.yujinsoft.shoppingmall.repository.ItemRepository;
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

    List<MultipartFile> createMultipartFiles(String name) throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for(int i=0;i<5;i++){
            String path = "/Users/hyunsu/Desktop";
            String imageName = name+ "_" + i + ".jpg";
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

        List<MultipartFile> multipartFileList = createMultipartFiles("test");
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

        List<MultipartFile> multipartFileList = createMultipartFiles("test");
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

        List<MultipartFile> multipartFileList = createMultipartFiles("test");
        Long itemId = itemService.saveItem(itemRegisterRequest,multipartFileList);

        ItemRegisterRequest savedItem = itemService.getItemDetail(itemId);
        savedItem.setItemNm("test edit");


        List<MultipartFile> updatedMultipartFileList = createMultipartFiles("test");
        Long updatedItemId = itemService.updateItem(savedItem,updatedMultipartFileList);

        Item updatedItem = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(updatedItem.getItemNm(), savedItem.getItemNm());
    }

    @Test
    @DisplayName("상품 이미지 수정 테스트")
    void updateItemImgTest() throws Exception{

        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest();
        itemRegisterRequest.setItemNm("test");
        itemRegisterRequest.setItemSellStatus(ItemSellStatus.SELL);
        itemRegisterRequest.setItemDetail("test item");
        itemRegisterRequest.setPrice(10000);
        itemRegisterRequest.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles("first");
        Long itemId = itemService.saveItem(itemRegisterRequest,multipartFileList);

        ItemRegisterRequest savedItem = itemService.getItemDetail(itemId);

        List<MultipartFile> multipartFileListSecond = createMultipartFiles("second");
        Long updatedItemId = itemService.updateItem(savedItem,multipartFileListSecond);

        List<ItemImg> updatedItemImgList = itemImgRepository.findByItemIdOrderByIdAsc(updatedItemId);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ updatedItemImgList.get(0).getOriImgName() : "+updatedItemImgList.get(0).getOriImgName() );

        assertEquals(updatedItemImgList.get(0).getOriImgName(), multipartFileListSecond.get(0).getOriginalFilename());

    }


}