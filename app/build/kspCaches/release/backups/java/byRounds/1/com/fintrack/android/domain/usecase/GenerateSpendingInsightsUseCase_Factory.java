package com.fintrack.android.domain.usecase;

import com.fintrack.android.domain.repository.BudgetRepository;
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
public final class GenerateSpendingInsightsUseCase_Factory implements Factory<GenerateSpendingInsightsUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public GenerateSpendingInsightsUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<BudgetRepository> budgetRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public GenerateSpendingInsightsUseCase get() {
    return newInstance(transactionRepositoryProvider.get(), budgetRepositoryProvider.get());
  }

  public static GenerateSpendingInsightsUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new GenerateSpendingInsightsUseCase_Factory(transactionRepositoryProvider, budgetRepositoryProvider);
  }

  public static GenerateSpendingInsightsUseCase newInstance(
      TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
    return new GenerateSpendingInsightsUseCase(transactionRepository, budgetRepository);
  }
}
