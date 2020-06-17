package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.UserModel;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserModel getUserById(Integer id);

    void register(UserModel userModel)
            throws BusinessException, NoSuchAlgorithmException;

    /**
     * @param telphone        用户手机
     * @param encryptPassword 加密后的密码
     * @return
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone, String encryptPassword)
            throws BusinessException, NoSuchAlgorithmException;
}
