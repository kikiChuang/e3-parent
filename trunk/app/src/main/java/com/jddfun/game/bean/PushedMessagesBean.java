package com.jddfun.game.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/10.
 */

public class PushedMessagesBean implements Serializable {

    /**
     * unreadCount : 0
     * messages : [{"id":116,"messageId":8,"title":"0330","content":"0330","messageStatus":1,"linkUrl":"http://www.jdd.com/football"},{"id":110,"messageId":7,"title":"3333333","content":"3333333","messageStatus":1,"linkUrl":"http://www.jdd.com/basketball"},{"id":112,"messageId":6,"title":"22222222","content":"22222222","messageStatus":1,"linkUrl":"http://www.jdd.com/football"},{"id":114,"messageId":5,"title":"111111","content":"111111","messageStatus":1,"linkUrl":"http://www.jdd.com/basketball"},{"id":108,"messageId":4,"title":"新游戏上线了","content":"新游戏上线了","messageStatus":1,"linkUrl":"http://www.jdd.com/basketball"},{"id":101,"messageId":3,"title":"cc","content":"cc","messageStatus":1,"linkUrl":"http://www.baidu.com"},{"id":100,"messageId":2,"title":"新版足球上线了","content":"新版足球上线了","messageStatus":1,"linkUrl":"http://www.jdd.com/basketball"},{"id":99,"messageId":1,"title":"新版篮球游戏上线了","content":"新版篮球游戏上线了","messageStatus":1,"linkUrl":"http://www.jdd.com/basketball"}]
     */

    int unReadCount;
    List<MessagesBean> messages;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean {
        /**
         * id : 116
         * messageId : 8
         * title : 0330
         * content : 0330
         * messageStatus : 1
         * linkUrl : http://www.jdd.com/football
         */

        int id;
        int messageId;
        String title;
        String content;
        int messageStatus;
        String linkUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getMessageStatus() {
            return messageStatus;
        }

        public void setMessageStatus(int messageStatus) {
            this.messageStatus = messageStatus;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }
}
