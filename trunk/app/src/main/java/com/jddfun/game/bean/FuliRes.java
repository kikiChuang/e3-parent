package com.jddfun.game.bean;

import java.util.List;

/**
 * Created by MACHINE on 2017/4/1.
 */

public class FuliRes {

    /**
     * code : 200
     * data : [{"id":10,"name":"福利","type":1,"url":"test5","icon":"welfare","corner":"","extraFlag":0,"sort":50,"remark":"test"},{"id":9,"name":"抽奖","type":1,"url":"test4","icon":"awards","corner":"","extraFlag":0,"sort":40,"remark":"test"},{"id":8,"name":"排行","type":1,"url":"test3","icon":"ranking","corner":"","extraFlag":0,"sort":30,"remark":"test"},{"id":7,"name":"任务","type":1,"url":"test2","icon":"task","corner":"","extraFlag":0,"sort":20,"remark":"test"},{"id":6,"name":"商城","type":1,"url":"test1","icon":"shop","corner":"","extraFlag":0,"sort":10,"remark":"test"}]
     */

    int code;
    List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * name : 福利
         * type : 1
         * url : test5
         * icon : welfare
         * corner :
         * extraFlag : 0
         * sort : 50
         * remark : test
         */

        int id;
        String name;
        int type;
        String url;
        String icon;
        String corner;
        int extraFlag;
        int sort;
        String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCorner() {
            return corner;
        }

        public void setCorner(String corner) {
            this.corner = corner;
        }

        public int getExtraFlag() {
            return extraFlag;
        }

        public void setExtraFlag(int extraFlag) {
            this.extraFlag = extraFlag;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
