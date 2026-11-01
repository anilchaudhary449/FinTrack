package com.fintrack.android.ui.screens.account;

import com.fintrack.android.domain.repository.AccountRepository;
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

  public AccountViewModel_Factory(Provider<AccountRepository> accountRepositoryProvider,
      Provider<ReconciliationRepository> reconciliationRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider) {
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.reconciliationRepositoryProvider = reconciliationRepositoryProvider;
    this.transferFundsUseCaseProvider = transferFundsUseCaseProvider;
  }

  @Override
  public AccountViewModel get() {
    return newInstance(accountRepositoryProvider.get(), reconciliationRepositoryProvider.get(), transferFundsUseCaseProvider.get());
  }

  public static AccountViewModel_Factory create(
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<ReconciliationRepository> reconciliationRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider) {
    return new AccountViewModel_Factory(accountRepositoryProvider, reconciliationRepositoryProvider, transferFundsUseCaseProvider);
  }

  public static AccountViewModel newInstance(AccountRepository accountRepository,
      ReconciliationRepository reconciliationRepository,
      TransferFundsUseCase transferFundsUseCase) {
    return new AccountViewModel(accountRepository, reconciliationRepository, transferFundsUseCase);
  }
}
