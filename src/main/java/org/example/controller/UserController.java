package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import org.example.controller.viewobject.UserVO;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true", allowedHeaders = "*") // 处理跨域请求
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 登录
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType login(@RequestBody Map<String, Object> params) throws BusinessException, NoSuchAlgorithmException {
        String password = params.getOrDefault("password", "").toString();
        String telphone = params.getOrDefault("telphone", "").toString();

        // 入参校验
        if(StringUtils.isEmpty(telphone)
                || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 校验密码是否正确
        UserModel userModel = userService.validateLogin(telphone, UserModel.EncodeByMd5(password));

        // 将登录成功后的信息加入到session中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        Map<String, Object> data = new HashMap<>();
        data.put("id", userModel.getId());
        return CommonReturnType.create(data);
    }

    // 注册
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType register(@RequestBody UserModel userModel) throws BusinessException, NoSuchAlgorithmException {

        // 验证手机号验证码对应
        String otpCode = userModel.getOtpCode();
        String sessionOtpCode = (String) httpServletRequest.getSession().getAttribute(userModel.getTelphone());

        System.out.println("otpCode: " + otpCode);
        System.out.println("sessionOtpCode: " + sessionOtpCode);

        if (!StringUtils.equals(otpCode, sessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "验证码错误");
        }

        // 用户注册流程
        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    // 一次性密码 One Time Password
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getOtp(@RequestBody Map<String, Object> params) {

        // 生成opt验证码 [0, 8999) + 1000 => [1000, 9999)
        Random random = new Random();

        String code = String.valueOf(1000 + random.nextInt(8999));
        System.out.println(params);
        String telphone = (String) params.get("telphone");

        System.out.println("telphone:" + telphone + " code:" + code);

        // 将验证码写入session
        httpServletRequest.getSession().setAttribute(telphone, code);
        return CommonReturnType.create(null);
    }

    // 获取用户信息
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);

        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    // 将领域模型转换为供UI使用的viewobject
    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }



}
