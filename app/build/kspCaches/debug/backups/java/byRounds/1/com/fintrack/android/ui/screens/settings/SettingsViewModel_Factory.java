package com.fintrack.android.ui.screens.settings;

import com.fintrack.android.data.export.JsonBackupManager;
import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.domain.repository.ExchangeRateRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<JsonBackupManager> jsonBackupManagerProvider;

  private final Provider<ExchangeRateRepository> exchangeRateRepositoryProvider;

  public SettingsViewModel_Factory(Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<JsonBackupManager> jsonBackupManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.jsonBackupManagerProvider = jsonBackupManagerProvider;
    this.exchangeRateRepositoryProvider = exchangeRateRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get(), jsonBackupManagerProvider.get(), exchangeRateRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<JsonBackupManager> jsonBackupManagerProvider,
      Provider<ExchangeRateRepository> exchangeRateRepositoryProvider) {
    return new SettingsViewModel_Factory(preferencesManagerProvider, jsonBackupManagerProvider, exchangeRateRepositoryProvider);
  }

  public static SettingsViewModel newInstance(UserPreferencesManager preferencesManager,
      JsonBackupManager jsonBackupManager, ExchangeRateRepository exchangeRateRepository) {
    return new SettingsViewModel(preferencesManager, jsonBackupManager, exchangeRateRepository);
  }
}
