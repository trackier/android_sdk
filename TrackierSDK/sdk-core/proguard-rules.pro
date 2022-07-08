#proguard rules for library
-keep public class com.trackier.sdk.TrackierSDK**{*;}
-keep public class com.trackier.sdk.TrackierSDKConfig**{*;}


#proguard  rules for intallreferrer
-keep public class com.android.installreferrer.* { *; }
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

