package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer ItemId, Integer amount) throws BusinessException;

}
