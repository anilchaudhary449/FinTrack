package com.fintrack.android.ui.screens.debt;

import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.DebtRepository;
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
public final class DebtViewModel_Factory implements Factory<DebtViewModel> {
  private final Provider<DebtRepository> debtRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  public DebtViewModel_Factory(Provider<DebtRepository> debtRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    this.debtRepositoryProvider = debtRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
  }

  @Override
  public DebtViewModel get() {
    return newInstance(debtRepositoryProvider.get(), accountRepositoryProvider.get());
  }

  public static DebtViewModel_Factory create(Provider<DebtRepository> debtRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    return new DebtViewModel_Factory(debtRepositoryProvider, accountRepositoryProvider);
  }

  public static DebtViewModel newInstance(DebtRepository debtRepository,
      AccountRepository accountRepository) {
    return new DebtViewModel(debtRepository, accountRepository);
  }
}
