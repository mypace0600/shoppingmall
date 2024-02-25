package com.yujinsoft.shoppingmall.controller;

import com.yujinsoft.shoppingmall.contract.ItemRegisterRequest;
import com.yujinsoft.shoppingmall.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}
