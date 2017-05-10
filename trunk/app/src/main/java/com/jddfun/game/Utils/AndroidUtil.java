package com.jddfun.game.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.jddfun.game.JDDApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class AndroidUtil {

    public static int SCREEN_HEIGHT = 1080;
    public static int SCREEN_WIDTH = 720;
    public static final float STANDARD_DENSITY = 2;//标准密度
    public static float SCREEN_DENSITY = STANDARD_DENSITY;

    /**
     * whether the mobile phone network is Connecting
     *
     * @param context
     * @return
     */
    public static boolean isConnectInternet(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public static String getUniqueId() {
        if (!TextUtils.isEmpty(getDiviceId())) {
            return getDiviceId();
        } else if (!TextUtils.isEmpty(getAndroidId(JDDApplication.mApp))) {
            return getAndroidId(JDDApplication.mApp);
        } else {
            return getOtherUniqueId(JDDApplication.mApp);
        }
    }

    /**
     * @param context
     * @return
     */
    public static String getOtherUniqueId(Context context) {
        UUID deviceUuid = new UUID(getSerialNumber(context).hashCode(), ((long) getMacAdress(context).hashCode() << 32) | getBrandInfoId().hashCode());
        return deviceUuid.toString();
    }

    private static String getBrandInfoId() {
        try {
            return "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAndroidId(Context context) {
        try {
            return Secure
                    .getString(context.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDiviceId() {
        try {
            TelephonyManager tm = (TelephonyManager) JDDApplication.mApp
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getMacAdress(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getDeviceWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDeviceHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static int getSDKVersionNumber() {
        int sdkVersion = 0;
        try {

            sdkVersion = Integer.valueOf(Build.VERSION.SDK);

        } catch (NumberFormatException e) {

            sdkVersion = 0;
        }
        return sdkVersion;
    }


    /**
     * getVersion
     */
    public static String getVersion() {
        String version = "0.0.0";

        PackageManager packageManager = JDDApplication.mApp.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    JDDApplication.mApp.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
        }

        return version;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static String getPakageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            // int versionCode = info.versionCode;
            // String versionName = info.versionName;
            String packageName = info.packageName;
            return packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);

            String ret = (String) info.applicationInfo.loadLabel(context.getPackageManager());
            return ret;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "未定义";
        }
    }


    /**
     * 显示SD卡不存在或空间不足的警告
     */
    public static void showSDCardUnavailableWarning() {
        Toast.makeText(JDDApplication.mApp.getApplicationContext(), "SD卡不存在或空间不足", Toast.LENGTH_SHORT).show();
    }

    /**
     * 存在并且容量大于10MB
     *
     * @return
     */
    public static boolean isSDCardExistAndNotFull() {
        return isSDCardExistAndNotFull(10);
    }

    /**
     * 存在并且容量大于指定MB
     *
     * @param minMB
     * @return
     */
    public static boolean isSDCardExistAndNotFull(long minMB) {
        if (!isSDCardExist()) {
            return false;
        }

        long leftMB = getSDFreeSize();
        return leftMB >= minMB;
    }

    /**
     * SD卡是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡可用空间 MB
     *
     * @return
     */
    public static long getSDFreeSize() {
        try {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();
            // 返回SD卡空闲大小
            // return freeBlocks * blockSize; //单位Byte
            // return (freeBlocks * blockSize)/1024; //单位KB
            return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * isappInstall
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }


    /**
     * 检测ImageView是否在用resourceId资源来显示
     *
     * @param imageView
     * @return
     */
    public static boolean isImageViewUseResourceId(ImageView imageView, boolean defaulBool) {
        try {
            int resource = 0;
            Class ImageViewClass = Class.forName("android.widget.ImageView");
            Field field = ImageViewClass.getDeclaredField("mResource");
            field.setAccessible(true);
            resource = field.getInt(imageView);
            if (resource > 0) {
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaulBool;
    }

    /**
     * 获取手机的唯一设备ID
     * 登陆和注册使用
     */
    public static String getMobileId() {
        String did = getUniqueId();
        return did + "v1.0" + "-" + "lxhelp";
    }

    /**
     * 获取系统名称
     *
     * @return
     */
    public static String getSystemProperty() {
        String line = null;
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = reader.readLine();
            if (TextUtils.isEmpty(line)) {
                return "UNKNOWN";
            }
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "UNKNOWN";
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 获取系统model
     *
     * @param context
     * @return
     */
    public static String getMobileModel(Context context) {
        String osMODEL = "";
        try {
            if (checkPhoneState(context)) {
                osMODEL = Build.MODEL;
                return osMODEL;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return osMODEL;
    }

    /**
     * check phone _state is readied ;
     *
     * @param context
     * @return
     */
    public static boolean checkPhoneState(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取当前的运营商类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") ||
                    operator.equals("46007")) {
                return "中国移动";
            } else if (operator.equals("46001")) {
                return "中国联通";
            } else if (operator.equals("46003")) {
                return "中国电信";
            }
        }
        return "未知";
    }

    /**
     * 获取当前联网模式
     *
     * @param context
     * @return
     */
    public static String getNetConnectType(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
            NetworkInfo gprs = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs

            if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
                return "wifi";
            } else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
                return "WWAN";
            }
            return "无网络";
        } catch (Exception e) {
            return "无网络";
        }
    }

    /**
     * 获取当前应用的版本名称
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = "1.0";
        try {
            Context context = JDDApplication.mApp;
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getSDPath() {
        String sdDir = null;
        if (isSDCardExist()) {
            sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
        }
        return sdDir;
    }

    public static String getExternalCacheDir() {
        if (!TextUtils.isEmpty(getSDPath())) {
            return getSDPath() + "/kanping/save";
        } else {
            return JDDApplication.mApp.getExternalCacheDir().getAbsolutePath();
        }
    }

    public static void copyTextToClipboard(Context context, String data) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        myClipboard.setText(data);
        ToastUtils.show(context, "链接已复制到剪切板");
    }

    //检查程序是否在后台运行
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }


    /**
     * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
     *
     * @param localVersion  本地版本号
     * @param onlineVersion 线上版本号
     * @return
     */
    public static boolean isAppNewVersion(String localVersion, String onlineVersion) {
        try {
            if (localVersion.equals(onlineVersion)) {
                return false;
            }
            String[] localArray = localVersion.split("\\.");
            String[] onlineArray = onlineVersion.split("\\.");

            int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
                    return true;
                } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
                    return false;
                }
                // 相等 比较下一组值
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
