package com.fintrack.android.ui.screens.transaction;

import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.CategoryRepository;
import com.fintrack.android.domain.repository.TransactionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class TransactionListViewModel_Factory implements Factory<TransactionListViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public TransactionListViewModel_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public TransactionListViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), accountRepositoryProvider.get(), categoryRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static TransactionListViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new TransactionListViewModel_Factory(transactionRepositoryProvider, accountRepositoryProvider, categoryRepositoryProvider, preferencesManagerProvider);
  }

  public static TransactionListViewModel newInstance(TransactionRepository transactionRepository,
      AccountRepository accountRepository, CategoryRepository categoryRepository,
      UserPreferencesManager preferencesManager) {
    return new TransactionListViewModel(transactionRepository, accountRepository, categoryRepository, preferencesManager);
  }
}
