#proguard rules for library
-keep public class com.trackier.sdk.TrackierSDK**{*;}
-keep public class com.trackier.sdk.TrackierSDKConfig**{*;}


#proguard  rules for intallreferrer
-keep class com.android.installreferrer.* { *; }
-keep public class com.android.installreferrer.api.** { *; }

# proguard- rules OkHttp, Retrofit and Moshi
-dontwarn okhttp3.**
-keep class retrofit2.** { *; }
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class com.squareup.moshi.**{*;}
-keep class com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory**{*;}

-keep @com.squareup.moshi.JsonQualifier interface *

# Kotlin and kotlinx proguard rules
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.reflect.jvm.internal.**
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-dontwarn kotlin.**
-keep class kotlinx.** {  volatile <fields>; }
-dontwarn kotlinx.**
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-keep class com.google.android.material.** { *; }

-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers,allowobfuscation @com.squareup.moshi.JsonClass class * { *; }

# Keep generated Moshi adapters
-keepnames class *JsonAdapter
-keepclassmembers class * {
    @com.squareup.moshi.* <fields>;
}

# Keep Retrofit interfaces & models
-keep interface com.trackier.sdk.** { *; }
-keep class com.trackier.sdk.** { *; }
-keepclassmembers class com.trackier.sdk.** {
    @com.squareup.moshi.JsonClass *;
}

# Keep WorkManager related classes (if used)
-keep class androidx.work.** { *; }

-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.jvm.internal.** { *; }
-keep class kotlin.** { *; }
-dontwarn kotlin.**
