package cn.e3mall.controller;

import cn.e3mall.common.pojo.EsayUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xuhongliang on 2017/7/12.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;

    }

    //商品列表
    @RequestMapping("/item/list")
    @ResponseBody
    public EsayUIDataGridResult getItemList(Integer page, Integer rows) {
        EsayUIDataGridResult list = itemService.getItemList(page, rows);
        return list;

    }


    //商品添加
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result getItemList(TbItem item, String desc) {
        E3Result e3Result = itemService.addItem(item, desc);
        return e3Result;

    }

}
