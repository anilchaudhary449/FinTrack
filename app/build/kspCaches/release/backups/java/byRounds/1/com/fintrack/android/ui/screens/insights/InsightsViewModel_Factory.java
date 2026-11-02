package com.fintrack.android.ui.screens.insights;

import com.fintrack.android.domain.usecase.GenerateSpendingInsightsUseCase;
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
public final class InsightsViewModel_Factory implements Factory<InsightsViewModel> {
  private final Provider<GenerateSpendingInsightsUseCase> generateSpendingInsightsUseCaseProvider;

  public InsightsViewModel_Factory(
      Provider<GenerateSpendingInsightsUseCase> generateSpendingInsightsUseCaseProvider) {
    this.generateSpendingInsightsUseCaseProvider = generateSpendingInsightsUseCaseProvider;
  }

  @Override
  public InsightsViewModel get() {
    return newInstance(generateSpendingInsightsUseCaseProvider.get());
  }

  public static InsightsViewModel_Factory create(
      Provider<GenerateSpendingInsightsUseCase> generateSpendingInsightsUseCaseProvider) {
    return new InsightsViewModel_Factory(generateSpendingInsightsUseCaseProvider);
  }

  public static InsightsViewModel newInstance(
      GenerateSpendingInsightsUseCase generateSpendingInsightsUseCase) {
    return new InsightsViewModel(generateSpendingInsightsUseCase);
  }
}
