package cn.e3mall.pagehelper;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by xuhongliang on 2017/7/12.
 */
public class PageHelpTest {

    @Test
    public void testPageHelp() throws Exception {
        //初始化spring容器 ，从容器中获得mapper代理对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
        PageHelper.startPage(1, 10);
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> list = mapper.selectByExample(tbItemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getPages());
        System.out.println(list.size());
        System.out.println(pageInfo.getTotal());

    }
}
