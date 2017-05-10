package com.jddfun.game.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.speech.RecognizerIntent;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class BaseUtil {

	//dp转换为像素px
	public static int dip2Px(Context context, float dp) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (dp * scale + 0.5f);  
	}  
	
	//像素px转换为dp
	public static int px2Dip(Context context, float px) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (px / scale + 0.5f);  
	} 
	
	/**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
	
	//此方法只是关闭软键盘
	public static void colseKeyboard(Activity context) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);            
		 if(imm.isActive() && context.getCurrentFocus()!=null){
		    if (context.getCurrentFocus().getWindowToken()!=null) {
		    	imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		    }             
		 }
	}
	
	//获取屏幕宽度
	@SuppressWarnings("deprecation")
	public static int getWidth(Activity activity){
		WindowManager wm = activity.getWindowManager();
	    return wm.getDefaultDisplay().getWidth();
	}
	
	//获取屏幕高度
	@SuppressWarnings("deprecation")
	public static int getHeight(Activity activity){
		WindowManager wm = activity.getWindowManager();
	    return wm.getDefaultDisplay().getHeight();
	}
	
	//显示键盘
	public static void showKeyboard(Context mContext){
		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);    
		//得到InputMethodManager的实例  
		if (imm.isActive()) {  
			//如果开启  
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);   
			//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的  
		}     
	}
	
	//关闭键盘
	public static void closeKeyboard(Activity mActivity){
		if(mActivity.getCurrentFocus()!=null)  
        {  
            ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE))  
            .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),  
                    InputMethodManager.HIDE_NOT_ALWAYS);   
        }  
	}
	
	/**
	 * 手机唯一码
	 * @return
	 */
	public static String getRegisterId(Context mContext){
		/**
		 * 1. The IMEI: 仅仅只对Android手机有效:
		 * 采用此种方法，需要在AndroidManifest.xml中加入一个许可：android.permission.READ_PHONE_STATE，并且用户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356（除非你有一个没有量产的手机（水货）它可能有无效的IMEI，如：0000000000000）。
		 */
		@SuppressWarnings("static-access")
		TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE); 
		String m_szImei = TelephonyMgr.getDeviceId(); 
		/**
		 * 2. Pseudo-Unique ID, 这个在任何Android手机中都有效
		 * 有一些特殊的情况，一些如平板电脑的设置没有通话功能，或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯一序列号之类的东西。这时你可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。但应当明白的是，出现类似情况的可能性基本可以忽略。要实现这一点，你可以使用Build类:
		 * 大多数的Build成员都是字符串形式的，我们只取他们的长度信息。我们取到13个数字，并在前面加上“35”。这样这个ID看起来就和15位IMEI一样了。
		 */
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI 
							Build.BOARD.length()%10 + 
							Build.BRAND.length()%10 + 
							Build.CPU_ABI.length()%10 + 
							Build.DEVICE.length()%10 + 
							Build.DISPLAY.length()%10 + 
							Build.HOST.length()%10 + 
							Build.ID.length()%10 + 
							Build.MANUFACTURER.length()%10 + 
							Build.MODEL.length()%10 + 
							Build.PRODUCT.length()%10 + 
							Build.TAGS.length()%10 + 
							Build.TYPE.length()%10 + 
							Build.USER.length()%10 ; //13 digits
		/**
		 * 3. The Android ID
		 * 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。并且，如果某个Andorid手机被Root过的话，这个ID也可以被任意改变。
		 * Returns: 9774d56d682e549c . 无需任何许可。
		 */
		String m_szAndroidID = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		/**
		 * 4. The WLAN MAC Address string
		 * 是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE 权限，否则这个地址会为null。
		 * Returns: 00:11:22:33:44:55 (这不是一个真实的地址。而且这个地址能轻易地被伪造。).WLan不必打开，就可读取些值。
		 */
		WifiManager wm = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
//		/**
//		 * 5. The BT MAC Address string
//		 * 只在有蓝牙的设备上运行。并且要加入android.permission.BLUETOOTH 权限.
//		 * Returns: 43:25:78:50:93:38 . 蓝牙没有必要打开，也能读取。
//		 */
//		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter      
//		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
//		String m_szBTMAC = m_BluetoothAdapter.getAddress();
		////////////////////////////////////////////////
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID+ m_szWLANMAC;      
			// compute md5     
		MessageDigest m = null;   
		try {
		  m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		 e.printStackTrace();   
		}    
		m.update(m_szLongID.getBytes(),0,m_szLongID.length());   
		// get md5 bytes   
		byte p_md5Data[] = m.digest();   
		// create a hex string   
		String m_szUniqueID = new String();   
		for (int i=0;i<p_md5Data.length;i++) {   
		     int b =  (0xFF & p_md5Data[i]);    
		// if it is a single digit, make sure it have 0 in front (proper padding)    
		    if (b <= 0xF) 
		        m_szUniqueID+="0";    
		// add number to string    
		    m_szUniqueID+=Integer.toHexString(b); 
		   }   // hex string to uppercase   
		m_szUniqueID = m_szUniqueID.toUpperCase();
		return m_szUniqueID;
	}
	
	//获取设备编号
	public static String getFacilitySn(){
		String device_model = Build.MODEL; // 设备型号   
		return device_model;
	}
	
	//获取设备名称
	@SuppressWarnings("static-access")
    public static String GetDeviceName(){
        return new Build().MODEL;
    }
	
	//获取设备系统
	public static String getFacilitySystem(){
		String version_release = Build.VERSION.RELEASE; // 设备的系统版本  
		return version_release;
	}
	
	@SuppressLint("SdCardPath")
 	private static final String PATH = "/sdcard/DCIM/yykaoo_logo.png";
	
	//图片压缩上传
    public static void copyFile(final Context context) {
 		new Thread(new Runnable() {  //开启线程上传文件
 			@Override
 			public void run() {
 				try {
				File file = new File(PATH);
				if(!file.exists()){
					copyBigDataToSD(context, PATH);
				}
 				} catch (IOException e) {
				e.printStackTrace();
			}
 			}
 		}).start();
 	}
     
 	private static void copyBigDataToSD(Context context,String strOutFileName) throws IOException{  
       InputStream myInput;  
       OutputStream myOutput = new FileOutputStream(strOutFileName);  
       myInput = context.getAssets().open("yykaoo_logo.png");  
       byte[] buffer = new byte[1024];  
       int length = myInput.read(buffer);
       while(length > 0){
           myOutput.write(buffer, 0, length); 
           length = myInput.read(buffer);
       }
       myOutput.flush();  
       myInput.close();  
       myOutput.close();        
   } 
 	

 	/**
     * 
     *  这个包名的程序是否在运行
     */
	  public static boolean isRunningApp(Context mContext) {
	        boolean isAppRunning = false;
	        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(100);
	        for (RunningTaskInfo info : list) {
	            if (info.topActivity.getPackageName().equals(mContext.getPackageName()) 
	            		&& info.baseActivity.getPackageName().equals(mContext.getPackageName())) {
	                isAppRunning = true;
	                break;
	            }
	        }
	        return isAppRunning;
	    }
 	
 	/**
 	 * 检测Android设备是否支持摄像机 
 	 * @param mContext
 	 * @return
 	 */
	public static boolean checkCameraDevice(Context mContext) {
		if (mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断音频是否可以用
	 * @param mContext
	 * @return
	 */
	public static boolean getResolveInfo(Context mContext) {
        Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        ResolveInfo ri = mContext.getPackageManager().
        resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        
        if(ri != null){
        	return true;
        }else{
        	return false;
        }
	}

 	
 	/**
	 * 拨打电话
	 * 
	 * @param con
	 * @param tel
	 * 
	 */
	public static void callPhoneNum(final Context con, final String tel) {
		// 取得相关系统服务
		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
			// 客服支持
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ tel));
			con.startActivity(intent);
		}else{
			Toast.makeText(con, "手机没有SIM卡", Toast.LENGTH_SHORT).show();
		}
	}
	
	/** 获得系统版本号 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 获得系统版本CODE */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/***
	 * 压缩图片
	 * @param fromFile
	 * @param myCaptureFile
	 * @param quality
	 */
	public static void transImage(String fromFile,File myCaptureFile,int quality){
		try{
			Bitmap bitmap = getimage(fromFile);
			if(bitmap != null){
				//save file
				FileOutputStream out = new FileOutputStream(myCaptureFile);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
				}
				if (!bitmap.isRecycled()) {
					bitmap.recycle();// 记得释放资源，否则会内存溢出
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//图片按比例大小压缩方法（根据路径获取图片并压缩
	public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    } 
	
	//质量压缩方法
	public static Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  

	/**
	 * 软件盘是否隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 获取InputMethodManager，隐藏软键盘
	 * 
	 * @param token
	 */
	public static void hideKeyboard(IBinder token, Activity activity) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	/**
	 * @Title: isFirstStart
	 * @Description: TODO(判断是否第一次启动App)
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isFirstStart(Context context) {
	    SharedPreferences preferences = context.getSharedPreferences("SHARE_APP_TAG", 0);
	    Boolean isFirst = preferences.getBoolean("first", true);
	    if (isFirst) {// 第一次
	        preferences.edit().putBoolean("first", false).commit();
	        return true;
	    } else {
	        return false;
	    }
	}
}
