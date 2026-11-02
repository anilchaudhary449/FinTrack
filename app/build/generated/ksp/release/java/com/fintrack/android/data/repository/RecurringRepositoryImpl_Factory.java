package com.fintrack.android.data.repository;

import com.fintrack.android.data.local.AppDatabase;
import com.fintrack.android.domain.repository.TransactionRepository;
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
public final class RecurringRepositoryImpl_Factory implements Factory<RecurringRepositoryImpl> {
  private final Provider<AppDatabase> databaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public RecurringRepositoryImpl_Factory(Provider<AppDatabase> databaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.databaseProvider = databaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public RecurringRepositoryImpl get() {
    return newInstance(databaseProvider.get(), transactionRepositoryProvider.get());
  }

  public static RecurringRepositoryImpl_Factory create(Provider<AppDatabase> databaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new RecurringRepositoryImpl_Factory(databaseProvider, transactionRepositoryProvider);
  }

  public static RecurringRepositoryImpl newInstance(AppDatabase database,
      TransactionRepository transactionRepository) {
    return new RecurringRepositoryImpl(database, transactionRepository);
  }
}
