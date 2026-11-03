package com.fintrack.android.ui.screens.account;

import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.ExchangeRateRepository;
import com.fintrack.android.domain.repository.ReconciliationRepository;
import com.fintrack.android.domain.usecase.TransferFundsUseCase;
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
public final class AccountViewModel_Factory implements Factory<AccountViewModel> {
  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<ReconciliationRepository> reconciliationRepositoryProvider;

  private final Provider<TransferFundsUseCase> transferFundsUseCaseProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<ExchangeRateRepository> exchangeRateRepositoryProvider;

  public AccountViewModel_Factory(Provider<AccountRepository> accountRepositoryProvider,
      Provider<ReconciliationRepository> reconciliationRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.reconciliationRepositoryProvider = reconciliationRepositoryProvider;
    this.transferFundsUseCaseProvider = transferFundsUseCaseProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.exchangeRateRepositoryProvider = exchangeRateRepositoryProvider;
  }

  @Override
  public AccountViewModel get() {
    return newInstance(accountRepositoryProvider.get(), reconciliationRepositoryProvider.get(), transferFundsUseCaseProvider.get(), preferencesManagerProvider.get(), exchangeRateRepositoryProvider.get());
  }

  public static AccountViewModel_Factory create(
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<ReconciliationRepository> reconciliationRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    return new AccountViewModel_Factory(accountRepositoryProvider, reconciliationRepositoryProvider, transferFundsUseCaseProvider, preferencesManagerProvider, exchangeRateRepositoryProvider);
  }

  public static AccountViewModel newInstance(AccountRepository accountRepository,
      ReconciliationRepository reconciliationRepository, TransferFundsUseCase transferFundsUseCase,
      UserPreferencesManager preferencesManager, ExchangeRateRepository exchangeRateRepository) {
    return new AccountViewModel(accountRepository, reconciliationRepository, transferFundsUseCase, preferencesManager, exchangeRateRepository);
  }
}
