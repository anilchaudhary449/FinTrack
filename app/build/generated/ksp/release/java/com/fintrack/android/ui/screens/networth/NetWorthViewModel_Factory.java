package com.fintrack.android.ui.screens.networth;

import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase;
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
public final class NetWorthViewModel_Factory implements Factory<NetWorthViewModel> {
  private final Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider;

  public NetWorthViewModel_Factory(
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider) {
    this.calculateNetWorthUseCaseProvider = calculateNetWorthUseCaseProvider;
  }

  @Override
  public NetWorthViewModel get() {
    return newInstance(calculateNetWorthUseCaseProvider.get());
  }

  public static NetWorthViewModel_Factory create(
      Provider<CalculateNetWorthUseCase> calculateNetWorthUseCaseProvider) {
    return new NetWorthViewModel_Factory(calculateNetWorthUseCaseProvider);
  }

  public static NetWorthViewModel newInstance(CalculateNetWorthUseCase calculateNetWorthUseCase) {
    return new NetWorthViewModel(calculateNetWorthUseCase);
  }
}
