package com.fintrack.android.ui.screens.audit;

import com.fintrack.android.domain.repository.AuditRepository;
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
public final class AuditViewModel_Factory implements Factory<AuditViewModel> {
  private final Provider<AuditRepository> auditRepositoryProvider;

  public AuditViewModel_Factory(Provider<AuditRepository> auditRepositoryProvider) {
    this.auditRepositoryProvider = auditRepositoryProvider;
  }

  @Override
  public AuditViewModel get() {
    return newInstance(auditRepositoryProvider.get());
  }

  public static AuditViewModel_Factory create(Provider<AuditRepository> auditRepositoryProvider) {
    return new AuditViewModel_Factory(auditRepositoryProvider);
  }

  public static AuditViewModel newInstance(AuditRepository auditRepository) {
    return new AuditViewModel(auditRepository);
  }
}
