package org.example.dao;

import org.example.dataobject.PromoInfoDao;

public interface PromoInfoDaoMapper {
    int insert(PromoInfoDao record);

    int insertSelective(PromoInfoDao record);

    PromoInfoDao selectByPrimaryKey(Integer id);

    PromoInfoDao selectByItemId(Integer itemId);

    int updateByPrimaryKeySelective(PromoInfoDao record);

    int updateByPrimaryKey(PromoInfoDao record);
}