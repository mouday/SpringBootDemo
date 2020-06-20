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

import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
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

    // 商品详情
    @GetMapping("/get")
    @ResponseBody
    public CommonReturnType create(@RequestParam Integer id) throws BusinessException {
        ItemModel itemModel =  itemService.getItemById(id);
        ItemVO itemVO = this.convertItemVOFromItemModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    // 商品列表
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType list() throws BusinessException {
        List<ItemModel> itemModels =  itemService.listItem();

        // 将model转为view
        List<ItemVO> itemVOS =  itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertItemVOFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOS);

    }

    public ItemVO convertItemVOFromItemModel(ItemModel itemModel){
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        return itemVO;
    }
}
