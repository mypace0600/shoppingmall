package com.yujinsoft.shoppingmall.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yujinsoft.shoppingmall.contract.ItemSearchRequest;
import com.yujinsoft.shoppingmall.contract.MainItemRequest;
import com.yujinsoft.shoppingmall.contract.QMainItemRequest;
import com.yujinsoft.shoppingmall.entity.Item;
import com.yujinsoft.shoppingmall.entity.QItem;
import com.yujinsoft.shoppingmall.entity.QItemImg;
import com.yujinsoft.shoppingmall.entity.enums.ItemSellStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchRequest itemSearchRequest, Pageable pageable) {
        List<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchRequest.getSearchDateType()),
                        searchSellStatusEq(itemSearchRequest.getSearchSellStatus()),
                        searchByLike(itemSearchRequest.getSearchBy(),itemSearchRequest.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchRequest.getSearchDateType()),
                        searchSellStatusEq(itemSearchRequest.getSearchSellStatus()),
                        searchByLike(itemSearchRequest.getSearchBy(), itemSearchRequest.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(results,pageable,total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<MainItemRequest> getMainItemPage(ItemSearchRequest itemSearchRequest, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemRequest> results = queryFactory
                .select(
                        new QMainItemRequest(
                        item.id,
                        item.itemNm,
                        item.itemDetail,
                        itemImg.imgUrl,
                        item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(itemNmLike(itemSearchRequest.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = results.size();
        return new PageImpl<>(results,pageable,total);
    }
}
