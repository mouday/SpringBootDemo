package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.dataobject.ItemStockDao;

public interface ItemStockDaoMapper {
    int insert(ItemStockDao record);

    int insertSelective(ItemStockDao record);

    ItemStockDao selectByPrimaryKey(Integer itemId);

    ItemStockDao selectByItemId(Integer id);

    int updateByPrimaryKeySelective(ItemStockDao record);

    int updateByPrimaryKey(ItemStockDao record);

    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
}