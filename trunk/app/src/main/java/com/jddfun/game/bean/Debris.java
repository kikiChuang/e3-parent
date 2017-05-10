package com.jddfun.game.bean;

import com.jddfun.game.Utils.Constants;

/**
 * Created by xuhongliang on 2017/4/26.
 */

public class Debris {


    //奖品图片
    String awardsImage;
    //奖品名称
    String awardsName;
    //兑换需要的数量
    int changeNum;
    //碎片id
    int fragmentId;
    //获取到的碎片数量
    int fragmentNum;
    //是否高亮
    boolean highlight;
    //碎片图片
    String image;
    //碎片名称
    String name;


    //文案1
    String remark1;

    //碎片描述
    String fragmentSource;


    public String getAwardsImage() {
        if (awardsImage.startsWith("http")) {
            return awardsImage;
        } else {
            return Constants.getUrl() + awardsImage;
        }
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

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public int getFragmentNum() {
        return fragmentNum;
    }

    public void setFragmentNum(int fragmentNum) {
        this.fragmentNum = fragmentNum;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getImage() {
        if (image.startsWith("http")) {
            return image;
        } else {
            return  Constants.getUrl() + image;
        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark1() {
        return "凑齐" + changeNum + "张可以兑换 " + awardsName;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }


    public boolean canCompound() {
        return fragmentNum >= changeNum;
    }

    public String getFragmentSource() {
        return fragmentSource;
    }

    public void setFragmentSource(String fragmentSource) {
        this.fragmentSource = fragmentSource;
    }
}
