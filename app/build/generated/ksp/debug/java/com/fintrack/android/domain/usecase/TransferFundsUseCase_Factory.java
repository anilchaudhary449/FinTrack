package com.fintrack.android.domain.usecase;

import com.fintrack.android.domain.repository.AccountRepository;
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
public final class TransferFundsUseCase_Factory implements Factory<TransferFundsUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  public TransferFundsUseCase_Factory(Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
  }

  @Override
  public TransferFundsUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), accountRepositoryProvider.get());
  }

  public static TransferFundsUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider) {
    return new TransferFundsUseCase_Factory(transactionRepositoryProvider, accountRepositoryProvider);
  }

  public static TransferFundsUseCase newInstance(TransactionRepository transactionRepository,
      AccountRepository accountRepository) {
    return new TransferFundsUseCase(transactionRepository, accountRepository);
  }
}
