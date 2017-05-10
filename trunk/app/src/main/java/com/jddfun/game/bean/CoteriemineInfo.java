package com.jddfun.game.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/12.
 */

public class CoteriemineInfo implements Serializable {


    /**
     * userId : 100504
     * list : [{"id":36,"userId":100504,"nickName":"","headImg":"http://192.168.101.242/cdn/bc00/1551/b832/4c37/bf36/c7ec/8a1b/bb95/bc001551b8324c37bf36c7ec8a1bbb95.png","title":null,"remark":"中大奖啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦啦啦啦啦啦啦拉拉啦啦啦啦啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯中大奖啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦啦啦啦啦啦啦拉拉啦啦啦啦啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦","detail":null,"status":1,"type":3,"praise":0,"havePraise":0,"awardsName":"0","createTime":"2017-04-21","platCoterieImgList":[{"id":20,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463","small":"http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463"},{"id":19,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/20e9/8797/a127/412a/a744/b557/a6ce/877a/20e98797a127412aa744b557a6ce877a","small":"http://192.168.101.242/cdn/20e9/8797/a127/412a/a744/b557/a6ce/877a/20e98797a127412aa744b557a6ce877a"},{"id":18,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/850e/39db/f3e0/48ca/8e58/a34a/67e2/a297/850e39dbf3e048ca8e58a34a67e2a297","small":"http://192.168.101.242/cdn/850e/39db/f3e0/48ca/8e58/a34a/67e2/a297/850e39dbf3e048ca8e58a34a67e2a297"}]}]
     */

    int userId;
    List<ListBean> list;

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

    public static class ListBean {
        /**
         * id : 36
         * userId : 100504
         * nickName :
         * headImg : http://192.168.101.242/cdn/bc00/1551/b832/4c37/bf36/c7ec/8a1b/bb95/bc001551b8324c37bf36c7ec8a1bbb95.png
         * title : null
         * remark : 中大奖啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦啦啦啦啦啦啦拉拉啦啦啦啦啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦咯啦咯啦咯啦咯中大奖啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦啦啦啦啦啦啦啦拉拉啦啦啦啦啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦
         * detail : null
         * status : 1
         * type : 3
         * praise : 0
         * havePraise : 0
         * awardsName : 0
         * createTime : 2017-04-21
         * platCoterieImgList : [{"id":20,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463","small":"http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463"},{"id":19,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/20e9/8797/a127/412a/a744/b557/a6ce/877a/20e98797a127412aa744b557a6ce877a","small":"http://192.168.101.242/cdn/20e9/8797/a127/412a/a744/b557/a6ce/877a/20e98797a127412aa744b557a6ce877a"},{"id":18,"createTime":"2017-04-21 15:05:57","updateTime":"2017-04-21 15:05:56","deleteFlag":0,"coterieId":36,"original":"http://192.168.101.242/cdn/850e/39db/f3e0/48ca/8e58/a34a/67e2/a297/850e39dbf3e048ca8e58a34a67e2a297","small":"http://192.168.101.242/cdn/850e/39db/f3e0/48ca/8e58/a34a/67e2/a297/850e39dbf3e048ca8e58a34a67e2a297"}]
         */

        int id;
        int userId;
        String nickName;
        String headImg;
        Object title;
        String content;
        Object detail;
        int status;
        int type;
        int praise;
        int havePraise;
        String awardsName;
        String createTime;
        List<PlatCoterieImgListBean> platCoterieImgList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getRemark() {
            return content;
        }

        public void setRemark(String content) {
            this.content = content;
        }

        public Object getDetail() {
            return detail;
        }

        public void setDetail(Object detail) {
            this.detail = detail;
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

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getHavePraise() {
            return havePraise;
        }

        public void setHavePraise(int havePraise) {
            this.havePraise = havePraise;
        }

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<PlatCoterieImgListBean> getPlatCoterieImgList() {
            return platCoterieImgList;
        }

        public void setPlatCoterieImgList(List<PlatCoterieImgListBean> platCoterieImgList) {
            this.platCoterieImgList = platCoterieImgList;
        }

        public static class PlatCoterieImgListBean {
            /**
             * id : 20
             * createTime : 2017-04-21 15:05:57
             * updateTime : 2017-04-21 15:05:56
             * deleteFlag : 0
             * coterieId : 36
             * original : http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463
             * small : http://192.168.101.242/cdn/6153/e17f/0ce3/4d92/ac81/dce1/17c4/7463/6153e17f0ce34d92ac81dce117c47463
             */

            int id;
            String createTime;
            String updateTime;
            int deleteFlag;
            int coterieId;
            String original;
            String small;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(int deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public int getCoterieId() {
                return coterieId;
            }

            public void setCoterieId(int coterieId) {
                this.coterieId = coterieId;
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
        }
    }
}
