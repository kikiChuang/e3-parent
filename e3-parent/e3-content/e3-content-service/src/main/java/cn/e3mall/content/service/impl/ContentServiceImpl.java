package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClientPool;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xuhongliang on 2017/7/17.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    TbContentMapper contentMapper;

    @Autowired
    JedisClientPool jedisClientPool;

    @Value("${CONTENT_LIST}")
    private String content_list;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        E3Result e3Result = E3Result.ok();
        return e3Result;
    }

    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //查询缓存
        try {
            String objectStr = jedisClientPool.hget(content_list, String.valueOf(cid));
            if (!StringUtils.isEmpty(objectStr)) {
                List<TbContent> list = JsonUtils.jsonToList(objectStr, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(tbContentExample);

        try {
            jedisClientPool.hset(content_list, String.valueOf(cid), JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
