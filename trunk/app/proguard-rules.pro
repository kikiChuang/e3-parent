# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\jdd\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-keepattributes *Annotation*
-keepattributes Signature
-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

#keep相关注解
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
-keep class javax.** { *; }
-dontwarn javax.**


#针对第三方包
#微信
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.** {*;}
#威富通
-dontwarn com.switfpass.pay.**
-keep class com.switfpass.pay.** {*;}
#紫微星
-dontwarn com.zwxpay.android.**
-keep class com.zwxpay.android.** {*;}
#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** {*;}
-keep class com.squareup.retrofit2.** {*;}
-keep class io.reactivex.** {*;}
#android support库
-dontwarn android.support.**
-keep class android.support.** {*;}
#circleimageview
-dontwarn de.hdodenhof.**
-keep class de.hdodenhof.** {*;}
#flycoTabLayout
-dontwarn com.flyco.tablayout.**
-keep class com.flyco.tablayout.** {*;}
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
#json
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**
#jsbridge
-dontwarn com.github.lzyzsd.**
-keep class com.github.lzyzsd.** {*;}
#imageselector
-dontwarn me.nereo.multi_image_selector.**
-keep class me.nereo.multi_image_selector.** {*;}
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.** {*;}
-dontwarn okio.**
-keep class okio.** {*;}
#picasso
-dontwarn com.squareup.**
-keep class com.squareup.** {*;}
#rx
-dontwarn rx.**
-keep class rx.** {*;}
#rxbinding
-dontwarn com.jakewharton.**
-keep class com.jakewharton.** {*;}
#rxlife
-dontwarn com.trello.**
-keep class com.trello.** {*;}
#rxper
-dontwarn com.tbruyelle.**
-keep class com.tbruyelle.** {*;}
#switchbutton
-dontwarn ch.ielse.**
-keep class com.tbruyelle.** {*;}
#tkrefreshlayout
-dontwarn com.lcodecore.**
-keep class com.lcodecore.** {*;}
#七鱼
-dontwarn com.qiyukf.**
-keep class com.qiyukf.** {*;}
#####butterknife#######
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-dontwarn butterknife.**
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#自己的包
-keep class com.jddfun.game.View.** {*;}
-keep class com.jddfun.game.bean.** {*;}
-keep class com.jddfun.game.event.** {*;}
-keep class com.jddfun.game.net.** {*;}
#WebView与JS接口类不混淆，否则执行不了
-dontwarn com.android.JsInterface.**
-keep class com.android.JsInterface.** {*; }
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#友盟推送混淆
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn org.apache.thrift.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}



#避免log打印输出
 -assumenosideeffects class android.util.Log {
      public static *** v(...);
      public static *** d(...);
      public static *** i(...);
      public static *** w(...);
 }

 #友盟统计混淆
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keep public class com.jddfun.game.R$*{
 public static final int *;
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 #贝富混淆
 -keep class com.xiaoxiaopay.mp.** {*;}
 -keep class com.xiaoxiaopay.net.** {*;}
 -keep class com.alipay.android.app.IAlixPay{*;}
 -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 -keep class com.alipay.sdk.app.PayTask{ public *;}
 -keep class com.alipay.sdk.app.AuthTask{ public *;}
 -keep class com.tencent.mm.sdk.**{*;}