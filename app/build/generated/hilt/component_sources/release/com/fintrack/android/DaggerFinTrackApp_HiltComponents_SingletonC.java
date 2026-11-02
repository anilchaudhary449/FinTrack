package com.fintrack.android;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import com.fintrack.android.data.export.JsonBackupManager;
import com.fintrack.android.data.local.AppDatabase;
import com.fintrack.android.data.preferences.UserPreferencesManager;
import com.fintrack.android.data.repository.AccountRepositoryImpl;
import com.fintrack.android.data.repository.AuditRepositoryImpl;
import com.fintrack.android.data.repository.BudgetRepositoryImpl;
import com.fintrack.android.data.repository.CategoryRepositoryImpl;
import com.fintrack.android.data.repository.DebtRepositoryImpl;
import com.fintrack.android.data.repository.ReconciliationRepositoryImpl;
import com.fintrack.android.data.repository.RecurringRepositoryImpl;
import com.fintrack.android.data.repository.SavingsRepositoryImpl;
import com.fintrack.android.data.repository.TransactionRepositoryImpl;
import com.fintrack.android.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.fintrack.android.di.DatabaseModule_ProvideApplicationScopeFactory;
import com.fintrack.android.domain.repository.RecurringRepository;
import com.fintrack.android.domain.usecase.CalculateNetWorthUseCase;
import com.fintrack.android.domain.usecase.GenerateSpendingInsightsUseCase;
import com.fintrack.android.domain.usecase.GetDashboardSummaryUseCase;
import com.fintrack.android.domain.usecase.TransferFundsUseCase;
import com.fintrack.android.ui.screens.account.AccountViewModel;
import com.fintrack.android.ui.screens.account.AccountViewModel_HiltModules;
import com.fintrack.android.ui.screens.audit.AuditViewModel;
import com.fintrack.android.ui.screens.audit.AuditViewModel_HiltModules;
import com.fintrack.android.ui.screens.budget.BudgetViewModel;
import com.fintrack.android.ui.screens.budget.BudgetViewModel_HiltModules;
import com.fintrack.android.ui.screens.calendar.CalendarViewModel;
import com.fintrack.android.ui.screens.calendar.CalendarViewModel_HiltModules;
import com.fintrack.android.ui.screens.dashboard.DashboardViewModel;
import com.fintrack.android.ui.screens.dashboard.DashboardViewModel_HiltModules;
import com.fintrack.android.ui.screens.debt.DebtViewModel;
import com.fintrack.android.ui.screens.debt.DebtViewModel_HiltModules;
import com.fintrack.android.ui.screens.insights.InsightsViewModel;
import com.fintrack.android.ui.screens.insights.InsightsViewModel_HiltModules;
import com.fintrack.android.ui.screens.networth.NetWorthViewModel;
import com.fintrack.android.ui.screens.networth.NetWorthViewModel_HiltModules;
import com.fintrack.android.ui.screens.onboarding.OnboardingViewModel;
import com.fintrack.android.ui.screens.onboarding.OnboardingViewModel_HiltModules;
import com.fintrack.android.ui.screens.reports.ReportsViewModel;
import com.fintrack.android.ui.screens.reports.ReportsViewModel_HiltModules;
import com.fintrack.android.ui.screens.savings.SavingsViewModel;
import com.fintrack.android.ui.screens.savings.SavingsViewModel_HiltModules;
import com.fintrack.android.ui.screens.settings.SettingsViewModel;
import com.fintrack.android.ui.screens.settings.SettingsViewModel_HiltModules;
import com.fintrack.android.ui.screens.splash.SplashViewModel;
import com.fintrack.android.ui.screens.splash.SplashViewModel_HiltModules;
import com.fintrack.android.ui.screens.transaction.AddEditTransactionViewModel;
import com.fintrack.android.ui.screens.transaction.AddEditTransactionViewModel_HiltModules;
import com.fintrack.android.ui.screens.transaction.TransactionDetailsViewModel;
import com.fintrack.android.ui.screens.transaction.TransactionDetailsViewModel_HiltModules;
import com.fintrack.android.ui.screens.transaction.TransactionListViewModel;
import com.fintrack.android.ui.screens.transaction.TransactionListViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

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
public final class DaggerFinTrackApp_HiltComponents_SingletonC {
  private DaggerFinTrackApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public FinTrackApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements FinTrackApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements FinTrackApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements FinTrackApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements FinTrackApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements FinTrackApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements FinTrackApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements FinTrackApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public FinTrackApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends FinTrackApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends FinTrackApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends FinTrackApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends FinTrackApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(16).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_account_AccountViewModel, AccountViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel, AddEditTransactionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_audit_AuditViewModel, AuditViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_budget_BudgetViewModel, BudgetViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_calendar_CalendarViewModel, CalendarViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_debt_DebtViewModel, DebtViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_insights_InsightsViewModel, InsightsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_networth_NetWorthViewModel, NetWorthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_reports_ReportsViewModel, ReportsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_savings_SavingsViewModel, SavingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel, TransactionDetailsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_TransactionListViewModel, TransactionListViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesManager(instance, singletonCImpl.userPreferencesManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_fintrack_android_ui_screens_audit_AuditViewModel = "com.fintrack.android.ui.screens.audit.AuditViewModel";

      static String com_fintrack_android_ui_screens_account_AccountViewModel = "com.fintrack.android.ui.screens.account.AccountViewModel";

      static String com_fintrack_android_ui_screens_debt_DebtViewModel = "com.fintrack.android.ui.screens.debt.DebtViewModel";

      static String com_fintrack_android_ui_screens_calendar_CalendarViewModel = "com.fintrack.android.ui.screens.calendar.CalendarViewModel";

      static String com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel = "com.fintrack.android.ui.screens.transaction.AddEditTransactionViewModel";

      static String com_fintrack_android_ui_screens_splash_SplashViewModel = "com.fintrack.android.ui.screens.splash.SplashViewModel";

      static String com_fintrack_android_ui_screens_dashboard_DashboardViewModel = "com.fintrack.android.ui.screens.dashboard.DashboardViewModel";

      static String com_fintrack_android_ui_screens_onboarding_OnboardingViewModel = "com.fintrack.android.ui.screens.onboarding.OnboardingViewModel";

      static String com_fintrack_android_ui_screens_savings_SavingsViewModel = "com.fintrack.android.ui.screens.savings.SavingsViewModel";

      static String com_fintrack_android_ui_screens_budget_BudgetViewModel = "com.fintrack.android.ui.screens.budget.BudgetViewModel";

      static String com_fintrack_android_ui_screens_insights_InsightsViewModel = "com.fintrack.android.ui.screens.insights.InsightsViewModel";

      static String com_fintrack_android_ui_screens_networth_NetWorthViewModel = "com.fintrack.android.ui.screens.networth.NetWorthViewModel";

      static String com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel = "com.fintrack.android.ui.screens.transaction.TransactionDetailsViewModel";

      static String com_fintrack_android_ui_screens_reports_ReportsViewModel = "com.fintrack.android.ui.screens.reports.ReportsViewModel";

      static String com_fintrack_android_ui_screens_settings_SettingsViewModel = "com.fintrack.android.ui.screens.settings.SettingsViewModel";

      static String com_fintrack_android_ui_screens_transaction_TransactionListViewModel = "com.fintrack.android.ui.screens.transaction.TransactionListViewModel";

      @KeepFieldType
      AuditViewModel com_fintrack_android_ui_screens_audit_AuditViewModel2;

      @KeepFieldType
      AccountViewModel com_fintrack_android_ui_screens_account_AccountViewModel2;

      @KeepFieldType
      DebtViewModel com_fintrack_android_ui_screens_debt_DebtViewModel2;

      @KeepFieldType
      CalendarViewModel com_fintrack_android_ui_screens_calendar_CalendarViewModel2;

      @KeepFieldType
      AddEditTransactionViewModel com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel2;

      @KeepFieldType
      SplashViewModel com_fintrack_android_ui_screens_splash_SplashViewModel2;

      @KeepFieldType
      DashboardViewModel com_fintrack_android_ui_screens_dashboard_DashboardViewModel2;

      @KeepFieldType
      OnboardingViewModel com_fintrack_android_ui_screens_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SavingsViewModel com_fintrack_android_ui_screens_savings_SavingsViewModel2;

      @KeepFieldType
      BudgetViewModel com_fintrack_android_ui_screens_budget_BudgetViewModel2;

      @KeepFieldType
      InsightsViewModel com_fintrack_android_ui_screens_insights_InsightsViewModel2;

      @KeepFieldType
      NetWorthViewModel com_fintrack_android_ui_screens_networth_NetWorthViewModel2;

      @KeepFieldType
      TransactionDetailsViewModel com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel2;

      @KeepFieldType
      ReportsViewModel com_fintrack_android_ui_screens_reports_ReportsViewModel2;

      @KeepFieldType
      SettingsViewModel com_fintrack_android_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      TransactionListViewModel com_fintrack_android_ui_screens_transaction_TransactionListViewModel2;
    }
  }

  private static final class ViewModelCImpl extends FinTrackApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AccountViewModel> accountViewModelProvider;

    private Provider<AddEditTransactionViewModel> addEditTransactionViewModelProvider;

    private Provider<AuditViewModel> auditViewModelProvider;

    private Provider<BudgetViewModel> budgetViewModelProvider;

    private Provider<CalendarViewModel> calendarViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<DebtViewModel> debtViewModelProvider;

    private Provider<InsightsViewModel> insightsViewModelProvider;

    private Provider<NetWorthViewModel> netWorthViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<ReportsViewModel> reportsViewModelProvider;

    private Provider<SavingsViewModel> savingsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<TransactionDetailsViewModel> transactionDetailsViewModelProvider;

    private Provider<TransactionListViewModel> transactionListViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private TransferFundsUseCase transferFundsUseCase() {
      return new TransferFundsUseCase(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get());
    }

    private GetDashboardSummaryUseCase getDashboardSummaryUseCase() {
      return new GetDashboardSummaryUseCase(singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.debtRepositoryImplProvider.get(), singletonCImpl.budgetRepositoryImplProvider.get(), singletonCImpl.recurringRepositoryImplProvider.get());
    }

    private CalculateNetWorthUseCase calculateNetWorthUseCase() {
      return new CalculateNetWorthUseCase(singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.debtRepositoryImplProvider.get(), singletonCImpl.savingsRepositoryImplProvider.get());
    }

    private GenerateSpendingInsightsUseCase generateSpendingInsightsUseCase() {
      return new GenerateSpendingInsightsUseCase(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.budgetRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.accountViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.addEditTransactionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.auditViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.budgetViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.calendarViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.debtViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.insightsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.netWorthViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.reportsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.savingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.transactionDetailsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.transactionListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(16).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_account_AccountViewModel, ((Provider) accountViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel, ((Provider) addEditTransactionViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_audit_AuditViewModel, ((Provider) auditViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_budget_BudgetViewModel, ((Provider) budgetViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_calendar_CalendarViewModel, ((Provider) calendarViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_debt_DebtViewModel, ((Provider) debtViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_insights_InsightsViewModel, ((Provider) insightsViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_networth_NetWorthViewModel, ((Provider) netWorthViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_reports_ReportsViewModel, ((Provider) reportsViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_savings_SavingsViewModel, ((Provider) savingsViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_splash_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel, ((Provider) transactionDetailsViewModelProvider)).put(LazyClassKeyProvider.com_fintrack_android_ui_screens_transaction_TransactionListViewModel, ((Provider) transactionListViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_fintrack_android_ui_screens_reports_ReportsViewModel = "com.fintrack.android.ui.screens.reports.ReportsViewModel";

      static String com_fintrack_android_ui_screens_dashboard_DashboardViewModel = "com.fintrack.android.ui.screens.dashboard.DashboardViewModel";

      static String com_fintrack_android_ui_screens_transaction_TransactionListViewModel = "com.fintrack.android.ui.screens.transaction.TransactionListViewModel";

      static String com_fintrack_android_ui_screens_account_AccountViewModel = "com.fintrack.android.ui.screens.account.AccountViewModel";

      static String com_fintrack_android_ui_screens_insights_InsightsViewModel = "com.fintrack.android.ui.screens.insights.InsightsViewModel";

      static String com_fintrack_android_ui_screens_settings_SettingsViewModel = "com.fintrack.android.ui.screens.settings.SettingsViewModel";

      static String com_fintrack_android_ui_screens_audit_AuditViewModel = "com.fintrack.android.ui.screens.audit.AuditViewModel";

      static String com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel = "com.fintrack.android.ui.screens.transaction.TransactionDetailsViewModel";

      static String com_fintrack_android_ui_screens_calendar_CalendarViewModel = "com.fintrack.android.ui.screens.calendar.CalendarViewModel";

      static String com_fintrack_android_ui_screens_onboarding_OnboardingViewModel = "com.fintrack.android.ui.screens.onboarding.OnboardingViewModel";

      static String com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel = "com.fintrack.android.ui.screens.transaction.AddEditTransactionViewModel";

      static String com_fintrack_android_ui_screens_savings_SavingsViewModel = "com.fintrack.android.ui.screens.savings.SavingsViewModel";

      static String com_fintrack_android_ui_screens_budget_BudgetViewModel = "com.fintrack.android.ui.screens.budget.BudgetViewModel";

      static String com_fintrack_android_ui_screens_splash_SplashViewModel = "com.fintrack.android.ui.screens.splash.SplashViewModel";

      static String com_fintrack_android_ui_screens_debt_DebtViewModel = "com.fintrack.android.ui.screens.debt.DebtViewModel";

      static String com_fintrack_android_ui_screens_networth_NetWorthViewModel = "com.fintrack.android.ui.screens.networth.NetWorthViewModel";

      @KeepFieldType
      ReportsViewModel com_fintrack_android_ui_screens_reports_ReportsViewModel2;

      @KeepFieldType
      DashboardViewModel com_fintrack_android_ui_screens_dashboard_DashboardViewModel2;

      @KeepFieldType
      TransactionListViewModel com_fintrack_android_ui_screens_transaction_TransactionListViewModel2;

      @KeepFieldType
      AccountViewModel com_fintrack_android_ui_screens_account_AccountViewModel2;

      @KeepFieldType
      InsightsViewModel com_fintrack_android_ui_screens_insights_InsightsViewModel2;

      @KeepFieldType
      SettingsViewModel com_fintrack_android_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      AuditViewModel com_fintrack_android_ui_screens_audit_AuditViewModel2;

      @KeepFieldType
      TransactionDetailsViewModel com_fintrack_android_ui_screens_transaction_TransactionDetailsViewModel2;

      @KeepFieldType
      CalendarViewModel com_fintrack_android_ui_screens_calendar_CalendarViewModel2;

      @KeepFieldType
      OnboardingViewModel com_fintrack_android_ui_screens_onboarding_OnboardingViewModel2;

      @KeepFieldType
      AddEditTransactionViewModel com_fintrack_android_ui_screens_transaction_AddEditTransactionViewModel2;

      @KeepFieldType
      SavingsViewModel com_fintrack_android_ui_screens_savings_SavingsViewModel2;

      @KeepFieldType
      BudgetViewModel com_fintrack_android_ui_screens_budget_BudgetViewModel2;

      @KeepFieldType
      SplashViewModel com_fintrack_android_ui_screens_splash_SplashViewModel2;

      @KeepFieldType
      DebtViewModel com_fintrack_android_ui_screens_debt_DebtViewModel2;

      @KeepFieldType
      NetWorthViewModel com_fintrack_android_ui_screens_networth_NetWorthViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.fintrack.android.ui.screens.account.AccountViewModel 
          return (T) new AccountViewModel(singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.reconciliationRepositoryImplProvider.get(), viewModelCImpl.transferFundsUseCase());

          case 1: // com.fintrack.android.ui.screens.transaction.AddEditTransactionViewModel 
          return (T) new AddEditTransactionViewModel(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.categoryRepositoryImplProvider.get(), viewModelCImpl.transferFundsUseCase(), viewModelCImpl.savedStateHandle);

          case 2: // com.fintrack.android.ui.screens.audit.AuditViewModel 
          return (T) new AuditViewModel(singletonCImpl.auditRepositoryImplProvider.get());

          case 3: // com.fintrack.android.ui.screens.budget.BudgetViewModel 
          return (T) new BudgetViewModel(singletonCImpl.budgetRepositoryImplProvider.get(), singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.categoryRepositoryImplProvider.get());

          case 4: // com.fintrack.android.ui.screens.calendar.CalendarViewModel 
          return (T) new CalendarViewModel(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.recurringRepositoryImplProvider.get(), singletonCImpl.debtRepositoryImplProvider.get());

          case 5: // com.fintrack.android.ui.screens.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(viewModelCImpl.getDashboardSummaryUseCase(), viewModelCImpl.calculateNetWorthUseCase(), singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.userPreferencesManagerProvider.get());

          case 6: // com.fintrack.android.ui.screens.debt.DebtViewModel 
          return (T) new DebtViewModel(singletonCImpl.debtRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get());

          case 7: // com.fintrack.android.ui.screens.insights.InsightsViewModel 
          return (T) new InsightsViewModel(viewModelCImpl.generateSpendingInsightsUseCase());

          case 8: // com.fintrack.android.ui.screens.networth.NetWorthViewModel 
          return (T) new NetWorthViewModel(viewModelCImpl.calculateNetWorthUseCase());

          case 9: // com.fintrack.android.ui.screens.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.userPreferencesManagerProvider.get());

          case 10: // com.fintrack.android.ui.screens.reports.ReportsViewModel 
          return (T) new ReportsViewModel(viewModelCImpl.getDashboardSummaryUseCase(), singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.userPreferencesManagerProvider.get());

          case 11: // com.fintrack.android.ui.screens.savings.SavingsViewModel 
          return (T) new SavingsViewModel(singletonCImpl.savingsRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get());

          case 12: // com.fintrack.android.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.userPreferencesManagerProvider.get(), singletonCImpl.jsonBackupManagerProvider.get());

          case 13: // com.fintrack.android.ui.screens.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.userPreferencesManagerProvider.get());

          case 14: // com.fintrack.android.ui.screens.transaction.TransactionDetailsViewModel 
          return (T) new TransactionDetailsViewModel(singletonCImpl.transactionRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 15: // com.fintrack.android.ui.screens.transaction.TransactionListViewModel 
          return (T) new TransactionListViewModel(singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.accountRepositoryImplProvider.get(), singletonCImpl.categoryRepositoryImplProvider.get(), singletonCImpl.userPreferencesManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends FinTrackApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends FinTrackApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends FinTrackApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<CoroutineScope> provideApplicationScopeProvider;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<TransactionRepositoryImpl> transactionRepositoryImplProvider;

    private Provider<RecurringRepositoryImpl> recurringRepositoryImplProvider;

    private Provider<UserPreferencesManager> userPreferencesManagerProvider;

    private Provider<AccountRepositoryImpl> accountRepositoryImplProvider;

    private Provider<ReconciliationRepositoryImpl> reconciliationRepositoryImplProvider;

    private Provider<CategoryRepositoryImpl> categoryRepositoryImplProvider;

    private Provider<AuditRepositoryImpl> auditRepositoryImplProvider;

    private Provider<BudgetRepositoryImpl> budgetRepositoryImplProvider;

    private Provider<DebtRepositoryImpl> debtRepositoryImplProvider;

    private Provider<SavingsRepositoryImpl> savingsRepositoryImplProvider;

    private Provider<JsonBackupManager> jsonBackupManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>emptyMap());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideApplicationScopeProvider = DoubleCheck.provider(new SwitchingProvider<CoroutineScope>(singletonCImpl, 2));
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.transactionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TransactionRepositoryImpl>(singletonCImpl, 3));
      this.recurringRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<RecurringRepositoryImpl>(singletonCImpl, 0));
      this.userPreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesManager>(singletonCImpl, 4));
      this.accountRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AccountRepositoryImpl>(singletonCImpl, 5));
      this.reconciliationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ReconciliationRepositoryImpl>(singletonCImpl, 6));
      this.categoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CategoryRepositoryImpl>(singletonCImpl, 7));
      this.auditRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuditRepositoryImpl>(singletonCImpl, 8));
      this.budgetRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<BudgetRepositoryImpl>(singletonCImpl, 9));
      this.debtRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<DebtRepositoryImpl>(singletonCImpl, 10));
      this.savingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SavingsRepositoryImpl>(singletonCImpl, 11));
      this.jsonBackupManagerProvider = DoubleCheck.provider(new SwitchingProvider<JsonBackupManager>(singletonCImpl, 12));
    }

    @Override
    public void injectFinTrackApp(FinTrackApp finTrackApp) {
      injectFinTrackApp2(finTrackApp);
    }

    @Override
    public RecurringRepository recurringRepository() {
      return recurringRepositoryImplProvider.get();
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private FinTrackApp injectFinTrackApp2(FinTrackApp instance) {
      FinTrackApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.fintrack.android.data.repository.RecurringRepositoryImpl 
          return (T) new RecurringRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get(), singletonCImpl.transactionRepositoryImplProvider.get());

          case 1: // com.fintrack.android.data.local.AppDatabase 
          return (T) DatabaseModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideApplicationScopeProvider.get());

          case 2: // kotlinx.coroutines.CoroutineScope 
          return (T) DatabaseModule_ProvideApplicationScopeFactory.provideApplicationScope();

          case 3: // com.fintrack.android.data.repository.TransactionRepositoryImpl 
          return (T) new TransactionRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 4: // com.fintrack.android.data.preferences.UserPreferencesManager 
          return (T) new UserPreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.fintrack.android.data.repository.AccountRepositoryImpl 
          return (T) new AccountRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 6: // com.fintrack.android.data.repository.ReconciliationRepositoryImpl 
          return (T) new ReconciliationRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 7: // com.fintrack.android.data.repository.CategoryRepositoryImpl 
          return (T) new CategoryRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 8: // com.fintrack.android.data.repository.AuditRepositoryImpl 
          return (T) new AuditRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 9: // com.fintrack.android.data.repository.BudgetRepositoryImpl 
          return (T) new BudgetRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 10: // com.fintrack.android.data.repository.DebtRepositoryImpl 
          return (T) new DebtRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 11: // com.fintrack.android.data.repository.SavingsRepositoryImpl 
          return (T) new SavingsRepositoryImpl(singletonCImpl.provideAppDatabaseProvider.get());

          case 12: // com.fintrack.android.data.export.JsonBackupManager 
          return (T) new JsonBackupManager(singletonCImpl.provideAppDatabaseProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
