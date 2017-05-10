package com.jddfun.game.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/12.
 */

public class CoterielistBean implements Serializable {

    RaidersBean raiders;
    TipsBean tips;
    int userId;
    List<ListBean> list;

    public RaidersBean getRaiders() {
        return raiders;
    }

    public void setRaiders(RaidersBean raiders) {
        this.raiders = raiders;
    }

    public TipsBean getTips() {
        return tips;
    }

    public void setTips(TipsBean tips) {
        this.tips = tips;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class RaidersBean {
        int awardsName;
        int havePraise;
        int id;
        String nickName;
        List<ListBean.PlatCoterieImgListBean> platCoterieImgList;
        int praise;
        String remark;
        int status;
        String title;
        int type;
        int userId;
        String headImg;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(int awardsName) {
            this.awardsName = awardsName;
        }

        public int getHavePraise() {
            return havePraise;
        }

        public void setHavePraise(int havePraise) {
            this.havePraise = havePraise;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public List<ListBean.PlatCoterieImgListBean> getPlatCoterieImgList() {
            return platCoterieImgList;
        }

        public void setPlatCoterieImgList(List<ListBean.PlatCoterieImgListBean> platCoterieImgList) {
            this.platCoterieImgList = platCoterieImgList;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class TipsBean {
        /**
         * awardsName : 1
         * havePraise : 1
         * id : 5
         * nickName : cccc
         * platCoterieImgList : 1
         * praise : 4
         * remark : 1
         * status : 3
         * title : 发生的速度阿斯大
         * type : 1
         * userId : 1
         */

        int awardsName;
        int havePraise;
        int id;
        String nickName;
        int platCoterieImgList;
        int praise;
        String remark;
        int status;
        String title;
        int type;
        int userId;

        public int getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(int awardsName) {
            this.awardsName = awardsName;
        }

        public int getHavePraise() {
            return havePraise;
        }

        public void setHavePraise(int havePraise) {
            this.havePraise = havePraise;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPlatCoterieImgList() {
            return platCoterieImgList;
        }

        public void setPlatCoterieImgList(int platCoterieImgList) {
            this.platCoterieImgList = platCoterieImgList;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class ListBean {
        /**
         * havePraise : 64308
         * platCoterieImgList : [{"coterieId":11,"createTime":"2017-03-24 13:45:54","deleteFlag":0,"id":7,"original":"http://192.168.101.241/cdn/1676/38f6/4613/4917/9b35/fe35/c9c0/a1ae/167638f6461349179b35fe35c9c0a1ae.png","small":"http://192.168.101.241/cdn/7d2a/d3a3/cab1/4281/9bf4/cea7/e52c/f3ce/7d2ad3a3cab142819bf4cea7e52cf3ce.png","updateTime":"2017-03-24 05:48:21"}]
         * remark : 1
         * status : 15867
         * type : 18520
         * awardsName : 888
         * id : 11
         * nickName : cccc
         * praise : 0
         * title : 卡手机的法律阿斯
         * userId : 1
         */

        int havePraise;
        String content;
        int status;
        int type;
        String awardsName;
        int id;
        String nickName;
        int praise;
        String title;
        int userId;
        String createTime;
        String headImg;


        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        List<PlatCoterieImgListBean> platCoterieImgList;

        public int getHavePraise() {
            return havePraise;
        }

        public void setHavePraise(int havePraise) {
            this.havePraise = havePraise;
        }

        public String getRemark() {
            return content;
        }

        public void setRemark(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<PlatCoterieImgListBean> getPlatCoterieImgList() {
            return platCoterieImgList;
        }

        public void setPlatCoterieImgList(List<PlatCoterieImgListBean> platCoterieImgList) {
            this.platCoterieImgList = platCoterieImgList;
        }

        public static class PlatCoterieImgListBean {
            /**
             * coterieId : 11
             * createTime : 2017-03-24 13:45:54
             * deleteFlag : 0
             * id : 7
             * original : http://192.168.101.241/cdn/1676/38f6/4613/4917/9b35/fe35/c9c0/a1ae/167638f6461349179b35fe35c9c0a1ae.png
             * small : http://192.168.101.241/cdn/7d2a/d3a3/cab1/4281/9bf4/cea7/e52c/f3ce/7d2ad3a3cab142819bf4cea7e52cf3ce.png
             * updateTime : 2017-03-24 05:48:21
             */

            int coterieId;
            String createTime;
            int deleteFlag;
            int id;
            String original;
            String small;
            String updateTime;

            public int getCoterieId() {
                return coterieId;
            }

            public void setCoterieId(int coterieId) {
                this.coterieId = coterieId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(int deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
