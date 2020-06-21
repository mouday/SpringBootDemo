package org.example.controller;

import org.example.controller.viewobject.ItemVO;
import org.example.error.BusinessException;
import org.example.response.CommonReturnType;
import org.example.service.ItemService;
import org.example.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", origins = {"*"})
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;

    // 创建商品
    @PostMapping("/create")
    @ResponseBody
    public CommonReturnType create(@RequestBody ItemModel itemModel) throws BusinessException {
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = this.convertItemVOFromItemModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);

    }

    // 商品详情
    @GetMapping("/get")
    @ResponseBody
    public CommonReturnType create(@RequestParam Integer id) throws BusinessException {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = this.convertItemVOFromItemModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    // 商品列表
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType list() throws BusinessException {
        List<ItemModel> itemModels = itemService.listItem();

        // 将model转为view
        List<ItemVO> itemVOS = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertItemVOFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOS);

    }

    private ItemVO convertItemVOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        if (itemModel.getPromoModel() != null) {
            // 有秒杀活动 正在进行或已经结束
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setStartTime(itemModel.getPromoModel().getStartTime().toString(formatter));
            itemVO.setEndTime(itemModel.getPromoModel().getEndTime().toString(formatter));
        } else {
            itemVO.setPromoStatus(0);
        }

        return itemVO;
    }
}
