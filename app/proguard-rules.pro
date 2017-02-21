# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk-windows/tools/proguard/proguard-android.txt
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

#
-target 1.7

# 指定代码的压缩级别
-optimizationpasses 5
-overloadaggressively
-repackageclasses ''
-allowaccessmodification

# 包明不混合大小写
-dontusemixedcaseclassnames

# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

# 优化  不优化输入的类文件
-dontoptimize

# 预校验
-dontpreverify

#不混淆输入的类文件  这个是默认不混淆的
-dontobfuscate

# 混淆时是否记录日志
-verbose

# 混淆时所采用的算法
# -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizations !code/simplification/arithmetic,!code/allocation/variable

#保护注解
-keepattributes *Annotation*

#android
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# support v4 and v7
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.support.v7.app.AppCompatActivity

#忽略警告
-ignorewarning
-dontshrink

#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature

## 记录生成的日志数据,gradle build时在本项目根目录输出##
# apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
# 未混淆的类和成员
-printseeds proguard/seeds.txt
# 列出从 apk 中删除的代码
-printusage proguard/unused.txt
# 混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#如果引用了v4或者v7包
-dontwarn android.support.**

#保持 native 方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 自定义view
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-dontwarn okio.**
-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

# retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** {*;}

# butterknife
-dontwarn butterknife.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep class rx.**{*;}
-keep class rx.internal.util.**{*;}
-dontwarn rx.**
-dontwarn rx.internal.util.**
