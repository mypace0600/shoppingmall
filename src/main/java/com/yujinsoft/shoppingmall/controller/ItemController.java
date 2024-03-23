package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.contract.ItemSearchRequest;
import com.yujinsoft.shoppingmall.contract.MainItemRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item/register")
    public String itemForm(Model model){
        model.addAttribute("itemRegisterRequest", new ItemRegisterRequest());
        return "/item/itemForm";
    }

    @PostMapping("/admin/item/register")
    public String itemRegister(
            @Valid @ModelAttribute("itemRegisterRequest") ItemRegisterRequest itemRegisterRequest,
            BindingResult bindingResult,
            Model model,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList
    ){
        if(bindingResult.hasErrors()){
            return "/item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemRegisterRequest.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력값입니다.");
            return "/item/itemForm";
        }

        try{
            itemService.saveItem(itemRegisterRequest, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 오류가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/admin/items";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemAdminDetail(@PathVariable("itemId") Long itemId, Model model){
        try{
            ItemRegisterRequest itemRegisterRequest = itemService.getItemDetail(itemId);
            model.addAttribute("itemRegisterRequest",itemRegisterRequest);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemRegisterRequest", new ItemRegisterRequest());
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(
            @Valid ItemRegisterRequest itemRegisterRequest,
            BindingResult bindingResult,
            @RequestParam(value="itemImgFile", required = false) List<MultipartFile> itemImgFileList,
            Model model
    ){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemRegisterRequest, itemImgFileList);
        } catch(Exception e){
            model.addAttribute("errorMessage","상품 수정 중 오류가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/admin/items";
    }

    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchRequest itemSearchRequest, Model model, @PathVariable(value = "page",required = false) Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,3);
        Page<Item> items = itemService.getAdminItems(itemSearchRequest,pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchRequest",itemSearchRequest);
        model.addAttribute("maxPage",5);

        return "item/itemManage";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId){
        ItemRegisterRequest item = itemService.getItemDetail(itemId);
        model.addAttribute("item", item);
        return "item/itemDetail";
    }

    @GetMapping(value="/")
    public String main(ItemSearchRequest itemSearchRequest, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemRequest> items = itemService.getMainItemPage(itemSearchRequest,pageable);
        model.addAttribute("items",items);
        if(null == itemSearchRequest.getSearchQuery()){
            itemSearchRequest.setSearchQuery("");
        }
        model.addAttribute("itemSearchRequest",itemSearchRequest);
        model.addAttribute("maxPage",5);
        return "main";
    }

}
