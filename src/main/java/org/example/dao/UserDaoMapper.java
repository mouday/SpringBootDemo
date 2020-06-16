package org.example.dao;

import org.example.dataobject.UserDao;

public interface UserDaoMapper {
    int insert(UserDao record);

    int insertSelective(UserDao record);

    UserDao selectByPrimaryKey(Integer id);

    UserDao selectByTelphone(String telphone);

    int updateByPrimaryKeySelective(UserDao record);

    int updateByPrimaryKey(UserDao record);
}