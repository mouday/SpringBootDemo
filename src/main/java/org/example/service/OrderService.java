package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId,  Integer amount) throws BusinessException;

    OrderModel getById(String id);

}
