/*
 * Copyright 2014 LianXi. All rights reserved.
 * LianXi PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @LogUtil.java - 2014年4月14日
 */

package com.jddfun.game.Utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * LogUtil
 *
 * @author OCEAN
 */
public class LogUtils {


    public static String TAG = Constants.PACKAGENAME;

    private static boolean IS_DEBUG = Constants.isDebug;

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, cleanStr(msg));
        }
    }

    public static void d(Class<?> className, String msg) {
        if (IS_DEBUG) {
            Log.d(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, cleanStr(msg));
        }
    }

    public static void i(Class<?> className, String msg) {
        if (IS_DEBUG) {
            Log.i(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            Log.w(tag, cleanStr(msg));
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (IS_DEBUG) {
            Log.w(tag, cleanStr(msg), tr);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, cleanStr(msg));
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (IS_DEBUG) {
            Log.e(tag, cleanStr(msg), tr);
        }
    }

    public static void w(Class<?> className, String msg) {
        if (IS_DEBUG) {
            Log.w(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void e(Class<?> className, String msg) {
        if (IS_DEBUG) {
            Log.e(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void e(Class<?> className, String msg, Throwable tr) {
        if (IS_DEBUG) {
            Log.e(className.getSimpleName(), cleanStr(msg), tr);
        }
    }

    public static void v(String logTag, String msg) {
        if (IS_DEBUG) {
            Log.v(logTag, msg);
        }
    }

    private static String cleanStr(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        if (msg.length() > 3000) {
            return msg.substring(0, 3000);
        }
        return msg;
    }


    /**
     * 设置日志输出标记
     *
     * @param tag 日志标记
     */
    public static void setTag(String tag) {
        debug(tag, "Changing log tag to %s", tag);
        TAG = tag;
    }

    /**
     * 输出verbose级别日志
     *
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void verbose(String format, Object... args) {
        if (IS_DEBUG) {
            android.util.Log.v(TAG, buildMessage(format, args));
        }
    }

    /**
     * 输出debug级别日志
     *
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void debug(String format, Object... args) {
        if (IS_DEBUG) {
            android.util.Log.d(TAG, buildMessage(format, args));
        }
    }

    /**
     * 输出info级别日志
     *
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void info(String format, Object... args) {
        if (IS_DEBUG) {
            android.util.Log.i(TAG, buildMessage(format, args));
        }
    }

    /**
     * 输出error级别日志
     *
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void error(String format, Object... args) {
        if (IS_DEBUG) {
            String msg = buildMessage(format, args);
            android.util.Log.e(TAG, msg);
        }
    }

    /**
     * 输出error级别日志
     *
     * @param tr     异常
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void error(Throwable tr, String format, Object... args) {
        String msg = buildMessage(format, args);
        android.util.Log.e(TAG, msg, tr);
    }

    /**
     * 输出warn级别日志
     *
     * @param format 日志格式
     * @param args   替换参数
     */
    public static void warn(String format, Object... args) {
        android.util.Log.w(TAG, buildMessage(format, args));
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || (args != null && args.length <= 0)) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

//        String caller = "<unknown>";
//        // Walk up the stack looking for the first caller outside of VolleyLog.
//        // It will be at least two frames up, so start there.
//        for (int i = 2; i < trace.length; i++) {
//            Class<?> clazz = trace[i].getClass();
//            if (!clazz.equals(LogUtils.class)) {
//                String callingClass = trace[i].getClassName();
//                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
//                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
//                caller = callingClass + "." + trace[i].getMethodName();
//                break;
//            }
//        }
//        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller,msg);
//        return String.format(Locale.US, "%s: %s", caller, msg);
        return String.format(Locale.US, "%s", msg);
    }

}