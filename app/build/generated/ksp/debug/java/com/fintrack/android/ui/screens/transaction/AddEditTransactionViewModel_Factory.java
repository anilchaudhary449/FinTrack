package com.fintrack.android.ui.screens.transaction;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.fintrack.android.domain.repository.AccountRepository;
import com.fintrack.android.domain.repository.CategoryRepository;
import com.fintrack.android.domain.repository.TransactionRepository;
import com.fintrack.android.domain.usecase.TransferFundsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AddEditTransactionViewModel_Factory implements Factory<AddEditTransactionViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<AccountRepository> accountRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<TransferFundsUseCase> transferFundsUseCaseProvider;

  private final Provider<Context> contextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditTransactionViewModel_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.accountRepositoryProvider = accountRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.transferFundsUseCaseProvider = transferFundsUseCaseProvider;
    this.contextProvider = contextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditTransactionViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), accountRepositoryProvider.get(), categoryRepositoryProvider.get(), transferFundsUseCaseProvider.get(), contextProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditTransactionViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<AccountRepository> accountRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TransferFundsUseCase> transferFundsUseCaseProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditTransactionViewModel_Factory(transactionRepositoryProvider, accountRepositoryProvider, categoryRepositoryProvider, transferFundsUseCaseProvider, contextProvider, savedStateHandleProvider);
  }

  public static AddEditTransactionViewModel newInstance(TransactionRepository transactionRepository,
      AccountRepository accountRepository, CategoryRepository categoryRepository,
      TransferFundsUseCase transferFundsUseCase, Context context,
      SavedStateHandle savedStateHandle) {
    return new AddEditTransactionViewModel(transactionRepository, accountRepository, categoryRepository, transferFundsUseCase, context, savedStateHandle);
  }
}
