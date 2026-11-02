package com.fintrack.android.domain.usecase;

import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.DebtRepository;
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
public final class CalculateNetWorthUseCase_Factory implements Factory<CalculateNetWorthUseCase> {
  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<DebtRepository> debtRepositoryProvider;

  private final Provider<SavingsRepository> savingsRepositoryProvider;

  public CalculateNetWorthUseCase_Factory(Provider<AccountRepository> accountRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider,
      Provider<SavingsRepository> savingsRepositoryProvider) {
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.debtRepositoryProvider = debtRepositoryProvider;
    this.savingsRepositoryProvider = savingsRepositoryProvider;
  }

  @Override
  public CalculateNetWorthUseCase get() {
    return newInstance(accountRepositoryProvider.get(), debtRepositoryProvider.get(), savingsRepositoryProvider.get());
  }

  public static CalculateNetWorthUseCase_Factory create(
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider,
      Provider<SavingsRepository> savingsRepositoryProvider) {
    return new CalculateNetWorthUseCase_Factory(accountRepositoryProvider, debtRepositoryProvider, savingsRepositoryProvider);
  }

  public static CalculateNetWorthUseCase newInstance(AccountRepository accountRepository,
      DebtRepository debtRepository, SavingsRepository savingsRepository) {
    return new CalculateNetWorthUseCase(accountRepository, debtRepository, savingsRepository);
  }
}
