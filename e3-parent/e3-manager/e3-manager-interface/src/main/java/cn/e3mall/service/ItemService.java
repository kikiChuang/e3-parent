package cn.e3mall.service;

import cn.e3mall.common.pojo.EsayUIDataGridResult;
import cn.e3mall.pojo.TbItem;

/**
 * Created by xuhongliang on 2017/7/12.
 */
public interface ItemService {
    TbItem getItemById(long itemId);

    EsayUIDataGridResult getItemList(int page, int rows);
}
