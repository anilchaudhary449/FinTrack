package com.fintrack.android.ui.screens.savings;

import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.SavingsRepository;
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
public final class SavingsViewModel_Factory implements Factory<SavingsViewModel> {
  private final Provider<SavingsRepository> savingsRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  public SavingsViewModel_Factory(Provider<SavingsRepository> savingsRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    this.savingsRepositoryProvider = savingsRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
  }

  @Override
  public SavingsViewModel get() {
    return newInstance(savingsRepositoryProvider.get(), accountRepositoryProvider.get());
  }

  public static SavingsViewModel_Factory create(
      Provider<SavingsRepository> savingsRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    return new SavingsViewModel_Factory(savingsRepositoryProvider, accountRepositoryProvider);
  }

  public static SavingsViewModel newInstance(SavingsRepository savingsRepository,
      AccountRepository accountRepository) {
    return new SavingsViewModel(savingsRepository, accountRepository);
  }
}
