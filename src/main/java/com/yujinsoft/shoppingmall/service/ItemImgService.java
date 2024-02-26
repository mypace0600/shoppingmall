package com.yujinsoft.shoppingmall.service;


import com.yujinsoft.shoppingmall.entity.ItemImg;
import com.yujinsoft.shoppingmall.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/"+imgName;
        }

        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);
    }

    public List<ItemImg> getItemImgList(Long itemId) {
        return itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    }

    public void updateItemImg(Long itemId, List<MultipartFile> itemImgFileList) throws Exception {
        List<ItemImg> savedItemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<Long> savedItemImgIdList = savedItemImgList.stream().map(ItemImg::getId).collect(Collectors.toList());

        if(!itemImgFileList.isEmpty()) {
            int index = 0;
            for (Long savedItemImgId : savedItemImgIdList) {
                ItemImg savedItemImg = itemImgRepository.findById(savedItemImgId)
                        .orElseThrow(EntityNotFoundException::new);

                if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                    fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
                }

                String oriImgName = itemImgFileList.get(index).getOriginalFilename();
                String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFileList.get(index).getBytes());
                String imgUrl = "/images/item/" + imgName;
                savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
                index++;
            }
        }
    }
}
