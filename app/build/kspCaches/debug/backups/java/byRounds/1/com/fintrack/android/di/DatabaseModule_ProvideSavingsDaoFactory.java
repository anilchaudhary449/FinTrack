package com.fintrack.android.di;

import com.fintrack.android.data.local.AppDatabase;
import com.fintrack.android.data.local.dao.SavingsDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideSavingsDaoFactory implements Factory<SavingsDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideSavingsDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SavingsDao get() {
    return provideSavingsDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideSavingsDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideSavingsDaoFactory(dbProvider);
  }

  public static SavingsDao provideSavingsDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSavingsDao(db));
  }
}
