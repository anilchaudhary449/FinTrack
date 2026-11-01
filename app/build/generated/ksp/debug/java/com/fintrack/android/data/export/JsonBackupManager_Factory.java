package com.fintrack.android.data.export;

import com.fintrack.android.data.local.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class JsonBackupManager_Factory implements Factory<JsonBackupManager> {
  private final Provider<AppDatabase> databaseProvider;

  public JsonBackupManager_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public JsonBackupManager get() {
    return newInstance(databaseProvider.get());
  }

  public static JsonBackupManager_Factory create(Provider<AppDatabase> databaseProvider) {
    return new JsonBackupManager_Factory(databaseProvider);
  }

  public static JsonBackupManager newInstance(AppDatabase database) {
    return new JsonBackupManager(database);
  }
}
