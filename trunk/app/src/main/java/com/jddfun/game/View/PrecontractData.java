package com.jddfun.game.View;

import com.jddfun.game.bean.Banner;
import com.jddfun.game.CustomView.ImageCycleView;

import java.util.ArrayList;

public class PrecontractData {


    public ArrayList getUrl(ArrayList<Banner> urlList) {
        ArrayList<String> imageUrl = new ArrayList<>();
        /**装在数据的集合  图片地址*/
        for (int i = 0; i < urlList.size(); i++) {
            imageUrl.add(urlList.get(i).getImage());
        }
        /**添加数据*/

        return imageUrl;
    }


    public ArrayList<String> getDesc(ArrayList<Banner> urlList) {
        /**装在数据的集合  文字描述*/
        ArrayList<String> imageDescList = new ArrayList<>();

        for (int i = 0; i < urlList.size(); i++) {
            imageDescList.add("");
        }


        return imageDescList;
    }


    /**
     * 初始化轮播图
     */
    public void initCarsuelView(ArrayList<String> imageDescList, ArrayList<String> urlList, ImageCycleView mCycleView, ImageCycleView.ImageCycleViewListener mAdCycleViewListener) {

        /**设置数据*/
        mCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
        mCycleView.startImageCycle();

    }


}
