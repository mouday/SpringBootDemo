package org.example.dao;

import org.example.dataobject.OrderInfoDao;

public interface OrderInfoDaoMapper {
    int insert(OrderInfoDao record);

    int insertSelective(OrderInfoDao record);

    OrderInfoDao selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderInfoDao record);

    int updateByPrimaryKey(OrderInfoDao record);
}