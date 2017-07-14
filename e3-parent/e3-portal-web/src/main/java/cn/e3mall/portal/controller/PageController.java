package cn.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuhongliang on 2017/7/12.
 */
@Controller
public class PageController {

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }


    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }
}
