package org.example.controller;

import org.example.controller.viewobject.OrderVo;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.example.service.ItemService;
import org.example.service.OrderService;
import org.example.service.PromoService;
import org.example.service.UserService;
import org.example.service.model.ItemModel;
import org.example.service.model.OrderModel;
import org.example.service.model.PromoModel;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
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
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private PromoService promoService;


    @Autowired
    private HttpServletRequest httpServletRequest;

    // 下单请求
    @GetMapping("/create")
    @ResponseBody
    public CommonReturnType createOrder(
            @RequestParam Integer itemId,
            @RequestParam Integer amount,
            @RequestParam(name = "promoId", required = false) Integer promoId
    ) throws BusinessException {
        // 检查用户是否登录
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        System.out.println(isLogin);

        if(isLogin == null || !isLogin){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户未登录");
        }

        // 获取用户信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户未登录");
        }

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, amount);

        return CommonReturnType.create(orderModel);
    }

    // 下单请求
    @GetMapping("/get")
    @ResponseBody
    public CommonReturnType get(@RequestParam String id) throws BusinessException {

        OrderModel orderModel = orderService.getById(id);
        ItemModel itemModel = itemService.getItemById(orderModel.getItemId());
        OrderVo orderVo = this.convertFromModels(orderModel, itemModel);
        return CommonReturnType.create(orderVo);
    }

    private OrderVo convertFromModels(OrderModel orderModel, ItemModel itemModel){
        if(orderModel == null){
            return null;
        }

        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(orderModel, orderVo);
        if(itemModel != null){
            orderVo.setItemTitle(itemModel.getTitle());
        }
        return orderVo;
    }




}
