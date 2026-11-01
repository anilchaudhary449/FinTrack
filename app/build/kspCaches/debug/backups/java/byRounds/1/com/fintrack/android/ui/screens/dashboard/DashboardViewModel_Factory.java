package com.fintrack.android.ui.screens.dashboard;

import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.TransactionRepository;
import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider;

  private final Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public DashboardViewModel_Factory(
      Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider,
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.getDashboardSummaryUseCaseProvider = getDashboardSummaryUseCaseProvider;
    this.calculateNetWorthUseCaseProvider = calculateNetWorthUseCaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(getDashboardSummaryUseCaseProvider.get(), calculateNetWorthUseCaseProvider.get(), transactionRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<GetDashboardSummaryUseCase> getDashboardSummaryUseCaseProvider,
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new DashboardViewModel_Factory(getDashboardSummaryUseCaseProvider, calculateNetWorthUseCaseProvider, transactionRepositoryProvider, preferencesManagerProvider);
  }

  public static DashboardViewModel newInstance(
      GetDashboardSummaryUseCase getDashboardSummaryUseCase,
      CalculateNetWorthUseCase calculateNetWorthUseCase,
      TransactionRepository transactionRepository, UserPreferencesManager preferencesManager) {
    return new DashboardViewModel(getDashboardSummaryUseCase, calculateNetWorthUseCase, transactionRepository, preferencesManager);
  }
}
