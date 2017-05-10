package com.jddfun.game.Utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jddfun.game.JDDApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import butterknife.internal.Utils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by MACHINE on 2017/3/23.
 */

public class UtilsTools {
    private static UtilsTools defaultInstance;
    private SharedPreferences sp;

    private final String FILE_NAME = "config";

    public static UtilsTools getInstance() {
        if (defaultInstance == null) {
            synchronized (Utils.class) {
                defaultInstance = new UtilsTools();
            }
        }
        return defaultInstance;
    }

    public static void showToast(CharSequence text, Context context) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void show(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(JDDApplication.mApp, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 生成json
     *
     * @param userBean
     * @return
     */
    public String toJson(Object userBean) {
        Gson gson = new Gson();
        String s = gson.toJson(userBean);
        return s;

    }

    /**
     * 解析json
     *
     * @param str json 数据
     * @param obj bean 对象
     * @return
     */
    public Object fromJson(String str, Object obj) {
        Gson gson = new Gson();
        Object o = gson.fromJson(str, obj.getClass());
        return o;
    }

    /**
     * 判断电话号码是否符合规则
     *
     * @param string
     * @return
     */
    public static boolean isPhoneRight(String string) {

//        String regx = "^1[34578]\\d{9}$";
//        Pattern p = Pattern.compile(regx);
//        Matcher matcher = p.matcher(string);

//        return matcher.matches();
        if (TextUtils.isDigitsOnly(string) && string.length() == 11) {
            return true;
        }
        return false;
    }

    /**
     * 保存状态值
     *
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取状态值
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }


    /**
     * 保存字符串
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取字符串
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


    public void putInt(String key, int value) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        if (sp == null) {
            sp = JDDApplication.mApp.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    /**
     * 版本号
     *
     * @param mContext
     * @return
     */

    public int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 版本名称
     *
     * @param mContext
     * @return
     */
    public String getVersionName(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        return "";
    }

    /**
     * 获取UUID
     */
    public String getUUid() {
        TelephonyManager tm = (TelephonyManager) JDDApplication.mApp.getSystemService(TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 设备的 唯一码
     *
     * @param @param  context
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: isDeviceID
     * @Description:
     */
    public String getDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }


    //获取uuid
    public String getLocalIPAddress() {

        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    //如果不是回环地址
                    if (!inetAddress.isLoopbackAddress()) {
                        //直接返回本地IP地址
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Error", ex.toString());
        }
        return null;
    }


    public static File saveImage(Context context, Bitmap bmp) {
        File appDir = new File(context.getApplicationContext().getFilesDir().getAbsolutePath(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    //判断网络是否可用
    public static  boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

