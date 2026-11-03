package com.fintrack.android.ui.screens.networth;

import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.ExchangeRateRepository;
import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase;
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
public final class NetWorthViewModel_Factory implements Factory<NetWorthViewModel> {
  private final Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<ExchangeRateRepository> exchangeRateRepositoryProvider;

  public NetWorthViewModel_Factory(
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    this.calculateNetWorthUseCaseProvider = calculateNetWorthUseCaseProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.exchangeRateRepositoryProvider = exchangeRateRepositoryProvider;
  }

  @Override
  public NetWorthViewModel get() {
    return newInstance(calculateNetWorthUseCaseProvider.get(), preferencesManagerProvider.get(), exchangeRateRepositoryProvider.get());
  }

  public static NetWorthViewModel_Factory create(
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    return new NetWorthViewModel_Factory(calculateNetWorthUseCaseProvider, preferencesManagerProvider, exchangeRateRepositoryProvider);
  }

  public static NetWorthViewModel newInstance(CalculateNetWorthUseCase calculateNetWorthUseCase,
      UserPreferencesManager preferencesManager, ExchangeRateRepository exchangeRateRepository) {
    return new NetWorthViewModel(calculateNetWorthUseCase, preferencesManager, exchangeRateRepository);
  }
}
