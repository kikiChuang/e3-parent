package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * Created by xuhongliang on 2017/7/13.
 */
public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(long parentId);
}
