package org.example.service.impl;

import org.example.dao.PromoInfoDaoMapper;
import org.example.dataobject.PromoInfoDao;
import org.example.service.PromoService;
import org.example.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoInfoDaoMapper promoInfoDaoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // 获取商品对应的秒杀活动信息
        PromoInfoDao promoInfoDao = promoInfoDaoMapper.selectByItemId(itemId);
        PromoModel promoModel= this.convertFromPromoInfoDao(promoInfoDao);

        if(promoModel == null){
            return null;
        }

        // 判断秒杀活动所处状态  1 还未开始 2进行中 3已结束
        if(promoModel.getStartTime().isAfterNow()){
            promoModel.setStatus(1);
        } else if(promoModel.getEndTime().isBeforeNow()){
            promoModel.setStatus(3);
        } else{
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromPromoInfoDao(PromoInfoDao promoInfoDao){
        if(promoInfoDao == null){
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoInfoDao, promoModel);
        promoModel.setStartTime(new DateTime(promoInfoDao.getStartDate()));
        promoModel.setEndTime(new DateTime(promoInfoDao.getEndDate()));
        return promoModel;
    }

}
