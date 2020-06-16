package org.example.dao;

import org.example.dataobject.ItemDao;

public interface ItemDaoMapper {
    int insert(ItemDao record);

    int insertSelective(ItemDao record);

    ItemDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDao record);

    int updateByPrimaryKey(ItemDao record);
}