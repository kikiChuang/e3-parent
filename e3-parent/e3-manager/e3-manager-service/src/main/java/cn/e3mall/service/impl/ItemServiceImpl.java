package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EsayUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {
        //根据example查询
//        TbItemExample example = new TbItemExample();
//        TbItemExample.Criteria criteria = example.createCriteria();
//        criteria.andIdEqualTo(itemId);
//        List<TbItem> list = itemMapper.selectByExample(example);
//        if (list != null && list.size() > 0) {
//            return list.get(0);
//        } else {
//            return null;
//        }
        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public EsayUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息,执行查询
        //取分页信息
        PageHelper.startPage(page, rows);
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(tbItemExample);
        PageInfo<TbItem> pageInfo = new PageInfo(list);
        long total = pageInfo.getTotal();
        EsayUIDataGridResult result = new EsayUIDataGridResult();
        result.setTotal(total);
        result.setRows(list);
        return result;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        item.setId(IDUtils.genItemId());
        //1正常，2下架，3删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(item.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        itemDescMapper.insert(tbItemDesc);
        return  E3Result.ok();
    }
}
