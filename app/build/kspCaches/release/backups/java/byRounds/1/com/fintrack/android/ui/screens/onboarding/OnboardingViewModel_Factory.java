package com.fintrack.android.ui.screens.onboarding;

import com.fintrack.android.data.preferences.UserPreferencesManager;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public OnboardingViewModel_Factory(Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new OnboardingViewModel_Factory(preferencesManagerProvider);
  }

  public static OnboardingViewModel newInstance(UserPreferencesManager preferencesManager) {
    return new OnboardingViewModel(preferencesManager);
  }
}
