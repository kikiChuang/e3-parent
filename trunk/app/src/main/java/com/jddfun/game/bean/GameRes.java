package com.jddfun.game.bean;

import java.util.List;

/**
 * Created by MACHINE on 2017/3/31.
 */

public class GameRes {

    /**
     * code : 200
     * data : [{"id":8,"name":"貂蝉保卫战","type":5,"url":"http://192.168.101.242/samguk","icon":"/cdn/8a2e/faf7/b2fa/4829/bdc2/6b53/966e/6779/8a2efaf7b2fa4829bdc26b53966e6779.jpg","remark":"貂蝉保卫战","redFlag":false},{"id":7,"name":"热血军团","type":4,"url":"http://192.168.101.242/legion","icon":"/cdn/game_icon/wars.png","remark":"入口","redFlag":false},{"id":1,"name":"飞镖入口","type":1,"url":"http://192.168.101.242/dart/","icon":"/cdn/game_icon/dart.png","remark":"入口","redFlag":true},{"id":2,"name":"桌球入口","type":2,"url":"http://192.168.101.242/billiards/","icon":"/cdn/game_icon/bill.png","remark":"入口","redFlag":false}]
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
         * id : 8
         * name : 貂蝉保卫战
         * type : 5
         * url : http://192.168.101.242/samguk
         * icon : /cdn/8a2e/faf7/b2fa/4829/bdc2/6b53/966e/6779/8a2efaf7b2fa4829bdc26b53966e6779.jpg
         * remark : 貂蝉保卫战
         * redFlag : false
         */

        int id;
        String name;
        int type;
        String url;
        String icon;
        String remark;
        boolean redFlag;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public boolean isRedFlag() {
            return redFlag;
        }

        public void setRedFlag(boolean redFlag) {
            this.redFlag = redFlag;
        }
    }
}
