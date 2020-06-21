package org.example.service.model;

import java.math.BigDecimal;

/**
 * 交易模型
 */
public class OrderModel {
    // 交易id 16位流水号
    private String id;

    // 用户id
    private Integer userId;

    // 商品id
    private Integer itemId;

    // 秒杀单价， 若非空则以秒杀价格下单
    private Integer promoId;

    // 购买商品时的单价
    private BigDecimal itemPrice;

    // 购买数量
    private Integer amount;

    // 购买金额
    private BigDecimal orderAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
