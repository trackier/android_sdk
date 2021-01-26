#proguard rules for library
-keep public class com.cloudstuff.trackiersdk.TrackierSDK.**{*;}
-keep public class com.cloudstuff.trackiersdk.TrackierSDKConfig.**{*;}
-keepclassmembers class com.cloudstuff.trackiersdk.TrackierSDKConfig  {
    *;
}

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
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *

# Kotlin and kotlinx proguard rules
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-dontwarn kotlin.**
-keep class kotlinx.** { *; }
-dontwarn kotlinx.**
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
