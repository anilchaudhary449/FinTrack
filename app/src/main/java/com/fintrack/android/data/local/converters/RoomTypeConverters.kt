package com.fintrack.android.data.local.converters

import androidx.room.TypeConverter
import com.fintrack.android.domain.model.*
import java.math.BigDecimal

class RoomTypeConverters {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    @TypeConverter
    fun fromTransactionType(type: TransactionType?): String? = type?.name

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? =
        value?.let { runCatching { TransactionType.valueOf(it) }.getOrDefault(TransactionType.EXPENSE) }

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod?): String? = method?.name

    @TypeConverter
    fun toPaymentMethod(value: String?): PaymentMethod? =
        value?.let { runCatching { PaymentMethod.valueOf(it) }.getOrDefault(PaymentMethod.CASH) }

    @TypeConverter
    fun fromAccountType(type: AccountType?): String? = type?.name

    @TypeConverter
    fun toAccountType(value: String?): AccountType? =
        value?.let { runCatching { AccountType.valueOf(it) }.getOrDefault(AccountType.CASH_WALLET) }

    @TypeConverter
    fun fromCategoryType(type: CategoryType?): String? = type?.name

    @TypeConverter
    fun toCategoryType(value: String?): CategoryType? =
        value?.let { runCatching { CategoryType.valueOf(it) }.getOrDefault(CategoryType.EXPENSE) }

    @TypeConverter
    fun fromRecurrenceFrequency(freq: RecurrenceFrequency?): String? = freq?.name

    @TypeConverter
    fun toRecurrenceFrequency(value: String?): RecurrenceFrequency? =
        value?.let { runCatching { RecurrenceFrequency.valueOf(it) }.getOrDefault(RecurrenceFrequency.MONTHLY) }

    @TypeConverter
    fun fromDebtType(type: DebtType?): String? = type?.name

    @TypeConverter
    fun toDebtType(value: String?): DebtType? =
        value?.let { runCatching { DebtType.valueOf(it) }.getOrDefault(DebtType.OWED_BY_ME) }

    @TypeConverter
    fun fromDebtStatus(status: DebtStatus?): String? = status?.name

    @TypeConverter
    fun toDebtStatus(value: String?): DebtStatus? =
        value?.let { runCatching { DebtStatus.valueOf(it) }.getOrDefault(DebtStatus.ACTIVE) }

    @TypeConverter
    fun fromAuditAction(action: AuditAction?): String? = action?.name

    @TypeConverter
    fun toAuditAction(value: String?): AuditAction? =
        value?.let { runCatching { AuditAction.valueOf(it) }.getOrDefault(AuditAction.CREATED) }
}

