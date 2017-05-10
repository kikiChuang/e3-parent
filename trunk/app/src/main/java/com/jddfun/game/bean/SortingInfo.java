package com.jddfun.game.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/11.
 */

public class SortingInfo implements Serializable {


    int eraserMe;
    int eraserTimes;
    int finalAmount;
    ProfitMeBean profitMe;
    ProfitNextBean profitNext;
    int richMe;
    List<EraserListBean> eraserList;
    List<RankingListBean> rankingList;
    List<RichListBean> richList;
    int useAmount;

    public int getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(int useAmount) {
        this.useAmount = useAmount;
    }

    public int getEraserMe() {
        return eraserMe;
    }

    public void setEraserMe(int eraserMe) {
        this.eraserMe = eraserMe;
    }

    public int getEraserTimes() {
        return eraserTimes;
    }

    public void setEraserTimes(int eraserTimes) {
        this.eraserTimes = eraserTimes;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public ProfitMeBean getProfitMe() {
        return profitMe;
    }

    public void setProfitMe(ProfitMeBean profitMe) {
        this.profitMe = profitMe;
    }

    public ProfitNextBean getProfitNext() {
        return profitNext;
    }

    public void setProfitNext(ProfitNextBean profitNext) {
        this.profitNext = profitNext;
    }

    public int getRichMe() {
        return richMe;
    }

    public void setRichMe(int richMe) {
        this.richMe = richMe;
    }

    public List<EraserListBean> getEraserList() {
        return eraserList;
    }

    public void setEraserList(List<EraserListBean> eraserList) {
        this.eraserList = eraserList;
    }

    public List<RankingListBean> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<RankingListBean> rankingList) {
        this.rankingList = rankingList;
    }

    public List<RichListBean> getRichList() {
        return richList;
    }

    public void setRichList(List<RichListBean> richList) {
        this.richList = richList;
    }

    public static class ProfitMeBean {
        /**
         * amount : 243076
         * awardsId : 1
         * awardsName : 50元彩金卡
         * index : 1
         * nickName : cccc
         * playTime : 1
         * reward : 1
         * userId : 1
         */

        int amount;
        int awardsId;
        String awardsName;
        int index;
        String nickName;
        int playTime;
        int reward;
        int userId;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAwardsId() {
            return awardsId;
        }

        public void setAwardsId(int awardsId) {
            this.awardsId = awardsId;
        }

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class ProfitNextBean {
        /**
         * amount : 243076
         * awardsImage : /cdn/profit/image_award_coin.png
         * awardsName : 50元彩金卡
         * downAmount : 26924
         * finalAwardsImage : /cdn/profit/iphone7.png
         * finalAwardsName : iPhone7
         * nextAwardsImage : /cdn/profit/image_award_coin.png
         * nextAwardsName : 80元彩金卡
         */

        int amount;
        String awardsImage;
        String awardsName;
        int downAmount;
        String finalAwardsImage;
        String finalAwardsName;
        String nextAwardsImage;
        String nextAwardsName;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAwardsImage() {
            return awardsImage;
        }

        public void setAwardsImage(String awardsImage) {
            this.awardsImage = awardsImage;
        }

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public int getDownAmount() {
            return downAmount;
        }

        public void setDownAmount(int downAmount) {
            this.downAmount = downAmount;
        }

        public String getFinalAwardsImage() {
            return finalAwardsImage;
        }

        public void setFinalAwardsImage(String finalAwardsImage) {
            this.finalAwardsImage = finalAwardsImage;
        }

        public String getFinalAwardsName() {
            return finalAwardsName;
        }

        public void setFinalAwardsName(String finalAwardsName) {
            this.finalAwardsName = finalAwardsName;
        }

        public String getNextAwardsImage() {
            return nextAwardsImage;
        }

        public void setNextAwardsImage(String nextAwardsImage) {
            this.nextAwardsImage = nextAwardsImage;
        }

        public String getNextAwardsName() {
            return nextAwardsName;
        }

        public void setNextAwardsName(String nextAwardsName) {
            this.nextAwardsName = nextAwardsName;
        }
    }

    public static class EraserListBean {
        /**
         * label : 0
         * type : game_eraser_cost
         * value : 1
         */

        String label;
        String type;
        String value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class RankingListBean {
        /**
         * reward : 1
         * amount : 243076
         * awardsId : 5
         * awardsName : 50元彩金卡
         * index : 1
         * nickName : cccc
         * playTime : 1491381255614
         * userId : 1
         */

        int reward;
        int amount;
        int awardsId;
        String awardsName;
        int index;
        String nickName;
        long playTime;
        int userId;

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAwardsId() {
            return awardsId;
        }

        public void setAwardsId(int awardsId) {
            this.awardsId = awardsId;
        }

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getPlayTime() {
            return playTime;
        }

        public void setPlayTime(long playTime) {
            this.playTime = playTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class RichListBean {
        /**
         * amount : 419209492
         * index : 1
         * nickName : cccc
         */

        Long amount;
        int index;
        String nickName;

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
