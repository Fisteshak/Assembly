-dontwarn org.apache.pdfbox.**
-dontwarn ch.qos.logback.**
-dontwarn org.apache.commons.**
-dontwarn io.ktor.network.**
-keep class org.apache.pdfbox.** { *; }
-keep class ch.qos.logback.** { *; }
-keep class org.apache.commons.** { *; }
-keep class io.ktor.network.* { *; }
-keep class org.openjfx.** { *; }


# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

