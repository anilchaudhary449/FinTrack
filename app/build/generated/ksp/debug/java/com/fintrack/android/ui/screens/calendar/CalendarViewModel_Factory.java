package com.fintrack.android.ui.screens.calendar;

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
public final class CalendarViewModel_Factory implements Factory<CalendarViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<RecurringRepository> recurringRepositoryProvider;

  private final Provider<DebtRepository> debtRepositoryProvider;

  public CalendarViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<RecurringRepository> recurringRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.recurringRepositoryProvider = recurringRepositoryProvider;
    this.debtRepositoryProvider = debtRepositoryProvider;
  }

  @Override
  public CalendarViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), recurringRepositoryProvider.get(), debtRepositoryProvider.get());
  }

  public static CalendarViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<RecurringRepository> recurringRepositoryProvider,
      Provider<DebtRepository> debtRepositoryProvider) {
    return new CalendarViewModel_Factory(transactionRepositoryProvider, recurringRepositoryProvider, debtRepositoryProvider);
  }

  public static CalendarViewModel newInstance(TransactionRepository transactionRepository,
      RecurringRepository recurringRepository, DebtRepository debtRepository) {
    return new CalendarViewModel(transactionRepository, recurringRepository, debtRepository);
  }
}
