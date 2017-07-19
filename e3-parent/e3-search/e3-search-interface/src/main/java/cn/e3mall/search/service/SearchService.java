package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * Created by xuhongliang on 2017/7/19.
 */
public interface SearchService {
    SearchResult search(String keyword, int page, int rows) throws Exception;
}
