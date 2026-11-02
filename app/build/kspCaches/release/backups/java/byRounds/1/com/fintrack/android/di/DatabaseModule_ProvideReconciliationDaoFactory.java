package com.fintrack.android.di;

import com.fintrack.android.data.local.AppDatabase;
import com.fintrack.android.data.local.dao.ReconciliationDao;
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
public final class DatabaseModule_ProvideReconciliationDaoFactory implements Factory<ReconciliationDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideReconciliationDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ReconciliationDao get() {
    return provideReconciliationDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideReconciliationDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideReconciliationDaoFactory(dbProvider);
  }

  public static ReconciliationDao provideReconciliationDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReconciliationDao(db));
  }
}
