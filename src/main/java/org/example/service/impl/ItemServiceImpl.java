package org.example.service.impl;

import org.example.dao.ItemDaoMapper;
import org.example.dao.ItemStockDaoMapper;
import org.example.dataobject.ItemDao;
import org.example.dataobject.ItemStockDao;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.service.ItemService;
import org.example.service.model.ItemModel;
import org.example.validator.ValidationResult;
import org.example.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDaoMapper itemDaoMapper;

    @Autowired
    private ItemStockDaoMapper itemStockDaoMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        // 入参校验
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMsg());
        }

        // 转换对象 itemModel -> dataObject
        ItemDao itemDao = this.convertFromItemModel(itemModel);

        // 写入数据库
        itemDaoMapper.insertSelective(itemDao);
        itemModel.setId(itemDao.getId());

        // 写入库存
        ItemStockDao itemStockDao = this.convertItemStockDaoFromItemModel(itemModel);
        itemStockDaoMapper.insertSelective(itemStockDao);

        // 返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDao> list = itemDaoMapper.selectList();
        List<ItemModel> itemModels = list.stream().map(itemDao -> {
           ItemStockDao itemStockDao = itemStockDaoMapper.selectByItemId(itemDao.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDao, itemStockDao);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModels;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDao itemDao = itemDaoMapper.selectByPrimaryKey(id);

        if (itemDao == null) {
            return null;
        }

        // 获得库存数量
        ItemStockDao itemStockDao = itemStockDaoMapper.selectByItemId(itemDao.getId());

        // 将dataobject -> model
        ItemModel itemModel = this.convertModelFromDataObject(itemDao, itemStockDao);

        return itemModel;
    }

    public ItemDao convertFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        ItemDao itemDao = new ItemDao();

        BeanUtils.copyProperties(itemModel, itemDao);

        return itemDao;
    }

    public ItemStockDao convertItemStockDaoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        ItemStockDao itemStockDao = new ItemStockDao();
        itemStockDao.setItemId(itemModel.getId());
        itemStockDao.setStock(itemModel.getStock());

        return itemStockDao;
    }

    public ItemModel convertModelFromDataObject(ItemDao itemDao, ItemStockDao itemStockDao){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDao, itemModel);
        itemModel.setStock(itemStockDao.getStock());

        return itemModel;
    }
}
