package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuhongliang on 2017/7/12.
 */
public class EsayUIDataGridResult implements Serializable {

    private long total;

    private List rows;


    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}