package org.example.controller;

import org.example.controller.viewobject.ItemVO;
import org.example.error.BusinessException;
import org.example.response.CommonReturnType;
import org.example.service.ItemService;
import org.example.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials="true", origins = {"*"})
public class ItemController extends BaseController{
    @Autowired
    private ItemService itemService;

    // 创建商品
    @PostMapping("/create")
    @ResponseBody
    public CommonReturnType create(@RequestBody ItemModel itemModel) throws BusinessException {
        ItemModel itemModelForReturn =  itemService.createItem(itemModel);
        ItemVO itemVO = this.convertItemVOFromItemModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);

    }

    public ItemVO convertItemVOFromItemModel(ItemModel itemModel){
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        return itemVO;
    }
}
