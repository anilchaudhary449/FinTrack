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
public final class CategoryRepositoryImpl_Factory implements Factory<CategoryRepositoryImpl> {
  private final Provider<AppDatabase> databaseProvider;

  public CategoryRepositoryImpl_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CategoryRepositoryImpl get() {
    return newInstance(databaseProvider.get());
  }

  public static CategoryRepositoryImpl_Factory create(Provider<AppDatabase> databaseProvider) {
    return new CategoryRepositoryImpl_Factory(databaseProvider);
  }

  public static CategoryRepositoryImpl newInstance(AppDatabase database) {
    return new CategoryRepositoryImpl(database);
  }
}
