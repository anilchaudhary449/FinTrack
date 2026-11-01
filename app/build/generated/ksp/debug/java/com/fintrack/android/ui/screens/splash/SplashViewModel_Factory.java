package com.fintrack.android.ui.screens.splash;

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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public SplashViewModel_Factory(Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static SplashViewModel_Factory create(
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new SplashViewModel_Factory(preferencesManagerProvider);
  }

  public static SplashViewModel newInstance(UserPreferencesManager preferencesManager) {
    return new SplashViewModel(preferencesManager);
  }
}
