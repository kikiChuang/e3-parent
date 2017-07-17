package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhongliang on 2017/7/14.
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
        EasyUITreeNode easyUITree;
        for (TbContentCategory contentCategory : categoryList) {
            easyUITree = new EasyUITreeNode();
            easyUITree.setId(contentCategory.getId());
            easyUITree.setText(contentCategory.getName());
            easyUITree.setState(contentCategory.getIsParent() ? "closed" : "open");
            easyUITreeNodes.add(easyUITree);
        }
        return easyUITreeNodes;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setParentId(parentId);
        contentCategoryMapper.insert(tbContentCategory);
        //判断父节点id
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        E3Result e3Result = E3Result.ok(tbContentCategory);
        return e3Result;
    }
}
