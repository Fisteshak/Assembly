-dontwarn org.apache.pdfbox.**
-dontwarn ch.qos.logback.**
-dontwarn org.apache.commons.**
-dontwarn io.ktor.network.**
-keep class org.apache.pdfbox.** { *; }
-keep class ch.qos.logback.** { *; }
-keep class org.apache.commons.** { *; }
-keep class io.ktor.network.* { *; }

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

