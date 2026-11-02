package com.fintrack.android.ui.screens.budget;

import com.fintrack.android.domain.repository.BudgetRepository;
import com.fintrack.android.domain.repository.CategoryRepository;
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
public final class BudgetViewModel_Factory implements Factory<BudgetViewModel> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public BudgetViewModel_Factory(Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public BudgetViewModel get() {
    return newInstance(budgetRepositoryProvider.get(), transactionRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static BudgetViewModel_Factory create(Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new BudgetViewModel_Factory(budgetRepositoryProvider, transactionRepositoryProvider, categoryRepositoryProvider);
  }

  public static BudgetViewModel newInstance(BudgetRepository budgetRepository,
      TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
    return new BudgetViewModel(budgetRepository, transactionRepository, categoryRepository);
  }
}
