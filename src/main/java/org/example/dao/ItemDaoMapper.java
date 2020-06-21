package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.dataobject.ItemDao;

import java.util.List;

public interface ItemDaoMapper {
    int insert(ItemDao record);

    int insertSelective(ItemDao record);

    ItemDao selectByPrimaryKey(Integer id);

    List<ItemDao> selectList();

    int updateByPrimaryKeySelective(ItemDao record);

    int updateByPrimaryKey(ItemDao record);

    int increaseSales(@Param("id") Integer id, @Param("amount") Integer amount);
}