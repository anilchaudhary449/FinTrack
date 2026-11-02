package com.fintrack.android;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class FinTrackApp_MembersInjector implements MembersInjector<FinTrackApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public FinTrackApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<FinTrackApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new FinTrackApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(FinTrackApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.fintrack.android.FinTrackApp.workerFactory")
  public static void injectWorkerFactory(FinTrackApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
