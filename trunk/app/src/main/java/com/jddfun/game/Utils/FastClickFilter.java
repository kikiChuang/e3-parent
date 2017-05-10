package com.jddfun.game.Utils;

/**
 * Created by peter on 2016/7/13.
 */
public class FastClickFilter {
    private static long lastClickTime;

    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
