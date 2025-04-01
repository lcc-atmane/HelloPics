# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ✅ Keep all classes to prevent ClassCastException issues
-keep class * { *; }

# ✅ Prevent warnings (useful for libraries)
-dontwarn **

# 🛠️ Fix Room Database Issues
-keep class androidx.room.** { *; }
-keep @androidx.room.* class * { *; }
-keep @androidx.sqlite.db.* class * { *; }


# 🔥 Fix Reflection-Based Libraries
-keepattributes *Annotation*
-keep class com.bumptech.glide.** { *; }
-keep class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.google.gson.** { *; }
-keep class com.google.gson.TypeAdapter
-keep class com.google.gson.TypeAdapterFactory
-keep class com.google.gson.JsonSerializer
-keep class com.google.gson.JsonDeserializer
-keep class com.squareup.retrofit2.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.internal.ads.** { *; }
-keep class com.google.common.util.concurrent.** { *; }
-keep class com.google.android.play.** { *; }
-keep class com.google.android.play.core.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }

# ✅ Keep UI Components
-keep public class * extends android.app.Activity
-keep public class * extends android.view.View { public <init>(android.content.Context); }

# 🛑 Prevent Android Logs from Being Removed
-assumenosideeffects class android.util.Log { *; }
-dontnote android.util.Log

# 🔄 Keep OpenCV (if using OpenCV)
-keep class org.opencv.** { *; }



# 🛠️ Keep Kotlin Metadata (Fix Kotlin Class Issues)
-keep class kotlin.Metadata { *; }

# 🚀 Keep ColorPickerView (if used)
-keep class top.defaults.colorpicker.** { *; }


