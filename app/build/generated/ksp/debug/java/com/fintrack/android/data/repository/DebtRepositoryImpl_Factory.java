package com.fintrack.android.data.repository;

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
public final class DebtRepositoryImpl_Factory implements Factory<DebtRepositoryImpl> {
  private final Provider<AppDatabase> databaseProvider;

  public DebtRepositoryImpl_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DebtRepositoryImpl get() {
    return newInstance(databaseProvider.get());
  }

  public static DebtRepositoryImpl_Factory create(Provider<AppDatabase> databaseProvider) {
    return new DebtRepositoryImpl_Factory(databaseProvider);
  }

  public static DebtRepositoryImpl newInstance(AppDatabase database) {
    return new DebtRepositoryImpl(database);
  }
}
