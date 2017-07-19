package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhongliang on 2017/7/19.
 */
@Repository
public class SearchDao {

    @Autowired
    SolrServer solrServer;


    public SearchResult search(SolrQuery solrQuery) throws SolrServerException {
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList solrDocuments = queryResponse.getResults();
        SearchResult searchResult = new SearchResult();
        searchResult.setRecordCount(solrDocuments.getNumFound());

        List<SearchItem> list = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlightingMap = queryResponse.getHighlighting();

        for (SolrDocument solrDocument : solrDocuments) {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));

            List<String> hilist = highlightingMap.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (hilist != null && hilist.size() > 0) {
                title = hilist.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            list.add(item);
        }
        searchResult.setItemList(list);
        return searchResult;
    }

}
