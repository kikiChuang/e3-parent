package com.jddfun.game.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.jddfun.game.JDDApplication;
import com.jddfun.game.bean.UserPersonalBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Xuhl on 2015/12/22.
 */
public class AccountManager {

    private static final String LOA_TAG = AccountManager.class.getSimpleName();

    public static final Object object = new Object();

    private static AccountManager instance;

    private UserPersonalBean currentUser;


    private static final String USER_CACHE_NAME = "jdd_user";

    public static AccountManager getInstance() {
        if (instance == null) {
            synchronized (object) {
                if (instance == null) {
                    instance = new AccountManager();
                }
            }
        }
        return instance;
    }

    public void setCurrentUser(UserPersonalBean user) {
        this.currentUser = user;
        writeToCache(user, USER_CACHE_NAME);
    }


    public UserPersonalBean getCurrentUser() {
        if (currentUser == null) {
            currentUser = getSerializeObj(USER_CACHE_NAME);
        }
        return currentUser;
    }

    public long getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getUserId();
        } else {
            return 0;
        }
    }


    public void setIsFirstShowPopNo() {
        SharedPreferences.Editor sharePrefEditor = JDDApplication.mApp.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE).edit();
        sharePrefEditor.putBoolean("show_pop", false).commit();
    }

    public boolean getIsShowPop() {
        SharedPreferences sharePrefEditor = JDDApplication.mApp.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        return sharePrefEditor.getBoolean("show_pop", true);
    }


    private String serializeObj(UserPersonalBean user) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(user);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    private UserPersonalBean getSerializeObj(String cacheKey) {
        UserPersonalBean cacheUser = null;
        try {
            SharedPreferences sp = JDDApplication.mApp.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            if (sp == null || !sp.contains(cacheKey)) {
                return null;
            }
            String str = sp.getString(cacheKey, null);
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            cacheUser = (UserPersonalBean) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheUser;
    }


    protected void writeToCache(UserPersonalBean user, String cacheKey) {
        try {
            SharedPreferences.Editor sharePrefEditor = JDDApplication.mApp.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE).edit();
            String result = serializeObj(user);
            sharePrefEditor.putString(cacheKey, result);
            sharePrefEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
