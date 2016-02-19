# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SoftWare\Android\sdk/tools/proguard/proguard-android.txt
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

-keep public class com.example.wuyuxi.webcam.activity.MainActivity$* {*;}

# see http://sourceforge.net/tracker/?func=detail&aid=2787465&group_id=54750&atid=474707
-optimizations !code/simplification/arithmetic
-optimizations !code/simplification/cast
-allowaccessmodification

# To prevent name conflict in incremental obfuscation.
-useuniqueclassmembernames

# dex does not like code run through proguard optimize and preverify steps.
-dontoptimize
-dontpreverify

# Don't obfuscate. We only need dead code striping.
# -dontobfuscate

# Add this flag in your package's own configuration if it's needed.
#-flattenpackagehierarchy

# Some classes in the libraries extend package private classes to chare common functionality
# that isn't explicitly part of the API
-dontskipnonpubliclibraryclasses -dontskipnonpubliclibraryclassmembers

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# class$ methods are inserted by some compilers to implement .class construct,
# see http://proguard.sourceforge.net/manual/examples.html#library
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

# Keep classes and methods that have the guava @VisibleForTesting annotation
-keep @com.google.common.annotations.VisibleForTesting class *
-keepclassmembers class * {
@com.google.common.annotations.VisibleForTesting *;
}

# Keep serializable classes and necessary members for serializable classes
# Copied from the ProGuard manual at http://proguard.sourceforge.net.
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
# See proguard-android.txt in the SDK package.
-dontwarn android.support.**

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
# support v4
-keep class android.support.** { *; }

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

# To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

# Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}

# eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# For Vitamio classes
-keep class io.vov.utils.** { *; }
-keep class io.vov.vitamio.** { *; }

# Logger
-keep class com.orhanobut.logger.**{ *;}

# adding this in to preserve line numbers so that the stack traces
# can be remapped
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-dontwarn android.content.Context
-dontskipnonpubliclibraryclassmembers