package org.example.dao;

import org.example.dataobject.SequenceInfoDao;

public interface SequenceInfoDaoMapper {
    int insert(SequenceInfoDao record);

    int insertSelective(SequenceInfoDao record);

    SequenceInfoDao selectByPrimaryKey(Integer id);

    SequenceInfoDao selectByName(String  name);

    int updateByPrimaryKeySelective(SequenceInfoDao record);

    int updateByPrimaryKey(SequenceInfoDao record);
}