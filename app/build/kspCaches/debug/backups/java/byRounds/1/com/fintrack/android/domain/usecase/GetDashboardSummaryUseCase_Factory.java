package com.fintrack.android.domain.usecase;

import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.BudgetRepository;
import com.fintrack.android.domain.repository.DebtRepository;
import com.fintrack.android.domain.repository.RecurringRepository;
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
public final class GetDashboardSummaryUseCase_Factory implements Factory<GetDashboardSummaryUseCase> {
  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<DebtRepository> debtRepositoryProvider;

  private final Provider<BudgetRepository> budgetRepositoryProvider;

  private final Provider<RecurringRepository> recurringRepositoryProvider;

  public GetDashboardSummaryUseCase_Factory(Provider<AccountRepository> accountRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider,
      Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<RecurringRepository> recurringRepositoryProvider) {
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.debtRepositoryProvider = debtRepositoryProvider;
    this.budgetRepositoryProvider = budgetRepositoryProvider;
    this.recurringRepositoryProvider = recurringRepositoryProvider;
  }

  @Override
  public GetDashboardSummaryUseCase get() {
    return newInstance(accountRepositoryProvider.get(), transactionRepositoryProvider.get(), debtRepositoryProvider.get(), budgetRepositoryProvider.get(), recurringRepositoryProvider.get());
  }

  public static GetDashboardSummaryUseCase_Factory create(
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider,
      Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<RecurringRepository> recurringRepositoryProvider) {
    return new GetDashboardSummaryUseCase_Factory(accountRepositoryProvider, transactionRepositoryProvider, debtRepositoryProvider, budgetRepositoryProvider, recurringRepositoryProvider);
  }

  public static GetDashboardSummaryUseCase newInstance(AccountRepository accountRepository,
      TransactionRepository transactionRepository, DebtRepository debtRepository,
      BudgetRepository budgetRepository, RecurringRepository recurringRepository) {
    return new GetDashboardSummaryUseCase(accountRepository, transactionRepository, debtRepository, budgetRepository, recurringRepository);
  }
}
