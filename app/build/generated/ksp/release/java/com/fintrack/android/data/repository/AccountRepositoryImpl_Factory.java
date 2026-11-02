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
public final class AccountRepositoryImpl_Factory implements Factory<AccountRepositoryImpl> {
  private final Provider<AppDatabase> databaseProvider;

  public AccountRepositoryImpl_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AccountRepositoryImpl get() {
    return newInstance(databaseProvider.get());
  }

  public static AccountRepositoryImpl_Factory create(Provider<AppDatabase> databaseProvider) {
    return new AccountRepositoryImpl_Factory(databaseProvider);
  }

  public static AccountRepositoryImpl newInstance(AppDatabase database) {
    return new AccountRepositoryImpl(database);
  }
}
