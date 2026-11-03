package com.fintrack.android.ui.screens.reports;

import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.ExchangeRateRepository;
import com.fintrack.android.domain.repository.TransactionRepository;
import com.fintrack.android.domain.usecase.GetDashboardSummaryUseCase;
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
public final class ReportsViewModel_Factory implements Factory<ReportsViewModel> {
  private final Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<ExchangeRateRepository> exchangeRateRepositoryProvider;

  public ReportsViewModel_Factory(
      Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    this.getDashboardSummaryUseCaseProvider = getDashboardSummaryUseCaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.exchangeRateRepositoryProvider = exchangeRateRepositoryProvider;
  }

  @Override
  public ReportsViewModel get() {
    return newInstance(getDashboardSummaryUseCaseProvider.get(), transactionRepositoryProvider.get(), accountRepositoryProvider.get(), preferencesManagerProvider.get(), exchangeRateRepositoryProvider.get());
  }

  public static ReportsViewModel_Factory create(
      Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    return new ReportsViewModel_Factory(getDashboardSummaryUseCaseProvider, transactionRepositoryProvider, accountRepositoryProvider, preferencesManagerProvider, exchangeRateRepositoryProvider);
  }

  public static ReportsViewModel newInstance(GetDashboardSummaryUseCase getDashboardSummaryUseCase,
      TransactionRepository transactionRepository, AccountRepository accountRepository,
      UserPreferencesManager preferencesManager, ExchangeRateRepository exchangeRateRepository) {
    return new ReportsViewModel(getDashboardSummaryUseCase, transactionRepository, accountRepository, preferencesManager, exchangeRateRepository);
  }
}
