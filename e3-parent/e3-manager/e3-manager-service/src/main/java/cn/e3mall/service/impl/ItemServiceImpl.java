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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
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

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicDestination;

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
        final long id =IDUtils.genItemId();
        item.setId(id);
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
        //发送消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(id+"");
            }
        });
        return  E3Result.ok();
    }
}
