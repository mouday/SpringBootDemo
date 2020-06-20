package org.example.service.impl;

import org.example.dao.OrderInfoDaoMapper;
import org.example.dao.SequenceInfoDaoMapper;
import org.example.dataobject.OrderInfoDao;
import org.example.dataobject.SequenceInfoDao;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.service.ItemService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.service.model.ItemModel;
import org.example.service.model.OrderModel;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderInfoDaoMapper orderInfoDaoMapper;

    @Autowired
    private SequenceInfoDaoMapper sequenceInfoDaoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        // 1、校验下单状态 商品是否存在，用户是否合法，数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不存在");
        }

        // 2、落单减库存，避免超卖
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3、下单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setId(this.generateOrderNo());
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderAmount(itemModel.getPrice().multiply(new BigDecimal(amount)));

        OrderInfoDao orderInfoDao = this.convertFromOrderModel(orderModel);
        orderInfoDaoMapper.insertSelective(orderInfoDao);
        return orderModel;
    }

    // 获取定单号 16位
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo() {
        // 定单号 16位 = 前8位时间年月日，6位自增序列， 最后2位分库分表
        StringBuilder stringBuilder = new StringBuilder();

        // 前8位时间年月日
        LocalDate now = LocalDate.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        //6位自增序列
        SequenceInfoDao sequenceInfoDao = sequenceInfoDaoMapper.selectByName("order_info");
        int sequence = sequenceInfoDao.getCurrentValue();
        sequenceInfoDao.setCurrentValue(sequence + sequenceInfoDao.getStep());
        sequenceInfoDaoMapper.updateByPrimaryKeySelective(sequenceInfoDao);
        String sequenceStr = String.valueOf(sequence);

        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }

        stringBuilder.append(sequenceStr);

        //最后2位分库分表
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderInfoDao convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }

        OrderInfoDao orderInfoDao = new OrderInfoDao();
        BeanUtils.copyProperties(orderModel, orderInfoDao);
        return orderInfoDao;
    }
}
