package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xuhongliang on 2017/7/18.
 */
@Controller
public class SearchItemController {
    @Autowired
    SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemSolr() {
        return searchItemService.importAllItems();
    }
}
