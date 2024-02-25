package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model){
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
    public String itemUpdate(@Valid ItemRegisterRequest itemRegisterRequest, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemRegisterRequest.getId() == null) {
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemRegisterRequest, itemImgFileList);
        } catch(Exception e){
            model.addAttribute("errorMessage","상품 수정 중 오류가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
}
