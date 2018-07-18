-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
# -dontoptimize
-dontpreverify
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
# 注解属性不混淆
# -keepattributes *Annotation*
# 数字签名信息不混淆
# -keepattributes Signature

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# JIN方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}


# keep annotated by NotProguard
-keep class com.ruomm.base.ioc.annotation.NotProguard
-keep @com.ruomm.base.ioc.annotation.NotProguard class * {*;}

-keepclassmembers class * {
	@com.ruomm.base.ioc.annotation.NotProguard <fields>;
    @com.ruomm.base.ioc.annotation.NotProguard <methods>;
}

# View的属性方法不混淆
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# OnClick方法不混淆
-keepclassmembers class * extends android.app.Activity{
   public void *(android.view.View);

}

# EventBus方法不混淆
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
   void onEvent*(***);
}

# 枚举类不混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Parcelable接口不混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# R文件属性不混淆
-keepclassmembers class **.R$* {
    public static <fields>;
}
# Entry不混淆
-keep class com.ruomm.base.view.** { *; }
-keep class com.ruomm.base.view.** { *; }
-keep class com.ruomm.base.common.greendao.** { *; }
-keep class com.zjsj.mchtapp.dal.** { *; }
-keep class com.zjsj.mchtapp.config.impl.** { *; }


# 序列化的对象不混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

# 第三方类库不混淆
# BaseAndroidLibrary
-dontwarn android.support.**


# 引用库
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }
-keep interface okio.** { *; }
-keep class org.bouncycastle.** { *; }
-keep interface org.bouncycastle.** { *; }
-keep class org.android.agoo.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.google.zxing.** { *; }
-keep class de.greenrobot.dao.** { *; }
-keep class com.baidu.** { *; }
-keep class com.alibaba.** { *;}
-keep class com.squareup.picasso.** { *;}
-keep class pl.droidsonroids.** { *;}

# 放弃提示的

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.bouncycastle.**
-dontwarn com.alibaba.**
-dontwarn com.baidu.**
-dontwarn com.squareup.**
-dontwarn java.awt.**
-dontwarn javax.servlet.**
-dontwarn org.springframework.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.**
-dontwarn com.alibaba.**
-dontwarn com.ut.mini.**
-dontwarn u.aly.**
