package com.fintrack.android.ui.screens.transaction;

import androidx.lifecycle.SavedStateHandle;
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
public final class TransactionDetailsViewModel_Factory implements Factory<TransactionDetailsViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public TransactionDetailsViewModel_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public TransactionDetailsViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static TransactionDetailsViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new TransactionDetailsViewModel_Factory(transactionRepositoryProvider, savedStateHandleProvider);
  }

  public static TransactionDetailsViewModel newInstance(TransactionRepository transactionRepository,
      SavedStateHandle savedStateHandle) {
    return new TransactionDetailsViewModel(transactionRepository, savedStateHandle);
  }
}
