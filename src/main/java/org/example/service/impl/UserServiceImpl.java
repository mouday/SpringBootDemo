package org.example.service.impl;

import com.alibaba.druid.util.StringUtils;
import org.example.dao.UserDaoMapper;
import org.example.dao.UserPasswordDaoMapper;
import org.example.dataobject.UserDao;
import org.example.dataobject.UserPasswordDao;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.example.validator.ValidationResult;
import org.example.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDaoMapper userDaoMapper;

    @Autowired
    private UserPasswordDaoMapper userPasswordDaoMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDao userDao = userDaoMapper.selectByPrimaryKey(id);
        if (userDao == null) {
            return null;
        }

        UserPasswordDao userPasswordDao = userPasswordDaoMapper.selectByUserId(userDao.getId());

        return convertFromDataObject(userDao, userPasswordDao);
    }

    @Override
    @Transactional // 事务
    public void register(UserModel userModel) throws BusinessException, NoSuchAlgorithmException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 必要的参数校验
        // if (StringUtils.isEmpty(userModel.getName())
        //         || userModel.getAge() == null
        //         || userModel.getGender() == null
        //         || StringUtils.isEmpty(userModel.getTelphone())
        // ) {
        //     throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        // }

        ValidationResult result = this.validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMsg());
        }

        // 保存用户信息
        UserDao userDao = convertFromUserModel(userModel);

        try {
            userDaoMapper.insertSelective(userDao);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已注册");
        }

        userModel.setId(userDao.getId());

        // 保存密码
        UserPasswordDao userPasswordDao = convertPasswordFromUserModel(userModel);
        userPasswordDaoMapper.insertSelective(userPasswordDao);
    }

    @Override
    public UserModel validateLogin(String telphone, String encryptPassword) throws BusinessException, NoSuchAlgorithmException {
        // 通过手机获取用户信息
        UserDao userDao = userDaoMapper.selectByTelphone(telphone);

        if(userDao == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        UserPasswordDao userPasswordDao = userPasswordDaoMapper.selectByUserId(userDao.getId());
        UserModel userModel = convertFromDataObject(userDao, userPasswordDao);

        // 比对用户输入密码和数据库密码
        if(!StringUtils.equals(userModel.getEncryptPassword(), encryptPassword)){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    public UserPasswordDao convertPasswordFromUserModel(UserModel userModel) throws NoSuchAlgorithmException {
        if (userModel == null) {
            return null;
        }

        UserPasswordDao userPasswordDao = new UserPasswordDao();
        userPasswordDao.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDao.setUserId(userModel.getId());

        return userPasswordDao;

    }

    // Model -> dataobject
    public UserDao convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserDao userDao = new UserDao();
        BeanUtils.copyProperties(userModel, userDao);
        return userDao;
    }

    // dataobject -> Model
    public UserModel convertFromDataObject(UserDao userDao, UserPasswordDao userPasswordDao) {
        if (userDao == null) {
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDao, userModel);

        if (userPasswordDao != null) {
            userModel.setEncryptPassword(userPasswordDao.getEncryptPassword());
        }

        return userModel;
    }
}
