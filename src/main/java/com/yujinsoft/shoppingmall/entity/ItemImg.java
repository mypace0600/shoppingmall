package com.yujinsoft.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;


@Table(name="item_img")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemImg {

    @Id
    @Column(name="item_img_id")
    @GeneratedValue
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName,String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
