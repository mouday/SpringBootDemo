package org.example.controller;

import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.example.service.OrderService;
import org.example.service.model.OrderModel;
import org.example.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", origins = {"*"})
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 下单请求
    @GetMapping("/create")
    @ResponseBody
    public CommonReturnType createOrder(
            @RequestParam Integer itemId,
            @RequestParam Integer amount
    ) throws BusinessException {
        // 检查用户是否登录
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户未登录");
        }

        // 获取用户信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount);

        return CommonReturnType.create(null);
    }
}
