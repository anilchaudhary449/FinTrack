package com.fintrack.android.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ExchangeRateRepositoryImpl_Factory implements Factory<ExchangeRateRepositoryImpl> {
  private final Provider<Context> contextProvider;

  public ExchangeRateRepositoryImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ExchangeRateRepositoryImpl get() {
    return newInstance(contextProvider.get());
  }

  public static ExchangeRateRepositoryImpl_Factory create(Provider<Context> contextProvider) {
    return new ExchangeRateRepositoryImpl_Factory(contextProvider);
  }

  public static ExchangeRateRepositoryImpl newInstance(Context context) {
    return new ExchangeRateRepositoryImpl(context);
  }
}
