package org.example.dao;

import org.example.dataobject.UserPasswordDao;

public interface UserPasswordDaoMapper {
    int insert(UserPasswordDao record);

    int insertSelective(UserPasswordDao record);

    UserPasswordDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPasswordDao record);

    int updateByPrimaryKey(UserPasswordDao record);

    UserPasswordDao selectByUserId(Integer userId);
}