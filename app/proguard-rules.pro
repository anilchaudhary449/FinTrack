# ProGuard / R8 configuration for FinTrack

# Keep Room generated code
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Hilt / Dagger
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
}
-dontwarn com.google.errorprone.annotations.**

# Keep models for Room & DataStore serialization
-keep class com.fintrack.android.data.local.entity.** { *; }
-keep class com.fintrack.android.domain.model.** { *; }

# Keep Kotlin Coroutines & Reflection
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

