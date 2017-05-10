package com.jddfun.game.bean;

import java.util.ArrayList;

/**
 * Created by xuhongliang on 2017/4/17.
 */

public class HomeItemData {

    public static int TYPE_HEAD = 1;

    public static int TYPE_GAME = 2;

    public static int TYPE_FOOT = 3;

    int type;

    UserPersonalBean userPersonalBean;

    ArrayList<Banner> banners;

    ArrayList<Notice> notices;

    Game game;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserPersonalBean getUserPersonalBean() {
        return userPersonalBean;
    }

    public void setUserPersonalBean(UserPersonalBean userPersonalBean) {
        this.userPersonalBean = userPersonalBean;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<Notice> getNotices() {
        return notices;
    }

    public void setNotices(ArrayList<Notice> notices) {
        this.notices = notices;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            HomeItemData data = (HomeItemData) obj;
            if (data.game != null && this.game != null) {
                return this.game.getId() == data.getGame().getId();
            } else {
                return data.getType() == this.getType();
            }
        } else {
            return true;
        }

    }

    @Override
    public int hashCode() {
        if (game != null) {
            return game.getId();
        } else {
            return type;
        }
    }
}
