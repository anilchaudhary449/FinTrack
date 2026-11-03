package com.fintrack.android.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fintrack.android.data.local.converters.RoomTypeConverters;
import com.fintrack.android.data.local.entity.AccountEntity;
import com.fintrack.android.data.local.entity.CategoryEntity;
import com.fintrack.android.data.local.entity.TransactionEntity;
import com.fintrack.android.data.local.entity.TransactionWithDetails;
import com.fintrack.android.domain.model.AccountType;
import com.fintrack.android.domain.model.CategoryType;
import com.fintrack.android.domain.model.PaymentMethod;
import com.fintrack.android.domain.model.TransactionType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __deletionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __updateAdapterOfTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTransactionById;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`amount`,`categoryId`,`accountId`,`toAccountId`,`paymentMethod`,`transactionDate`,`notes`,`receiptUri`,`currency`,`exchangeRate`,`recurringTransactionId`,`debtId`,`savingsGoalId`,`isReconciled`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __roomTypeConverters.fromTransactionType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        statement.bindLong(5, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getToAccountId());
        }
        final String _tmp_2 = __roomTypeConverters.fromPaymentMethod(entity.getPaymentMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        statement.bindLong(8, entity.getTransactionDate());
        statement.bindString(9, entity.getNotes());
        if (entity.getReceiptUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getReceiptUri());
        }
        statement.bindString(11, entity.getCurrency());
        final String _tmp_3 = __roomTypeConverters.fromBigDecimal(entity.getExchangeRate());
        if (_tmp_3 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_3);
        }
        if (entity.getRecurringTransactionId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getRecurringTransactionId());
        }
        if (entity.getDebtId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getDebtId());
        }
        if (entity.getSavingsGoalId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getSavingsGoalId());
        }
        final int _tmp_4 = entity.isReconciled() ? 1 : 0;
        statement.bindLong(16, _tmp_4);
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`type` = ?,`amount` = ?,`categoryId` = ?,`accountId` = ?,`toAccountId` = ?,`paymentMethod` = ?,`transactionDate` = ?,`notes` = ?,`receiptUri` = ?,`currency` = ?,`exchangeRate` = ?,`recurringTransactionId` = ?,`debtId` = ?,`savingsGoalId` = ?,`isReconciled` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __roomTypeConverters.fromTransactionType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        statement.bindLong(5, entity.getAccountId());
        if (entity.getToAccountId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getToAccountId());
        }
        final String _tmp_2 = __roomTypeConverters.fromPaymentMethod(entity.getPaymentMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        statement.bindLong(8, entity.getTransactionDate());
        statement.bindString(9, entity.getNotes());
        if (entity.getReceiptUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getReceiptUri());
        }
        statement.bindString(11, entity.getCurrency());
        final String _tmp_3 = __roomTypeConverters.fromBigDecimal(entity.getExchangeRate());
        if (_tmp_3 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_3);
        }
        if (entity.getRecurringTransactionId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getRecurringTransactionId());
        }
        if (entity.getDebtId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getDebtId());
        }
        if (entity.getSavingsGoalId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getSavingsGoalId());
        }
        final int _tmp_4 = entity.isReconciled() ? 1 : 0;
        statement.bindLong(16, _tmp_4);
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
        statement.bindLong(19, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTransactionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTransaction(final TransactionEntity transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransactionById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTransactionById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTransactionById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionWithDetails>> getAllTransactionsWithDetailsFlow() {
    final String _sql = "SELECT `transactions`.`id` AS `id`, `transactions`.`type` AS `type`, `transactions`.`amount` AS `amount`, `transactions`.`categoryId` AS `categoryId`, `transactions`.`accountId` AS `accountId`, `transactions`.`toAccountId` AS `toAccountId`, `transactions`.`paymentMethod` AS `paymentMethod`, `transactions`.`transactionDate` AS `transactionDate`, `transactions`.`notes` AS `notes`, `transactions`.`receiptUri` AS `receiptUri`, `transactions`.`currency` AS `currency`, `transactions`.`exchangeRate` AS `exchangeRate`, `transactions`.`recurringTransactionId` AS `recurringTransactionId`, `transactions`.`debtId` AS `debtId`, `transactions`.`savingsGoalId` AS `savingsGoalId`, `transactions`.`isReconciled` AS `isReconciled`, `transactions`.`createdAt` AS `createdAt`, `transactions`.`updatedAt` AS `updatedAt` FROM transactions ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = 0;
            final int _cursorIndexOfType = 1;
            final int _cursorIndexOfAmount = 2;
            final int _cursorIndexOfCategoryId = 3;
            final int _cursorIndexOfAccountId = 4;
            final int _cursorIndexOfToAccountId = 5;
            final int _cursorIndexOfPaymentMethod = 6;
            final int _cursorIndexOfTransactionDate = 7;
            final int _cursorIndexOfNotes = 8;
            final int _cursorIndexOfReceiptUri = 9;
            final int _cursorIndexOfCurrency = 10;
            final int _cursorIndexOfExchangeRate = 11;
            final int _cursorIndexOfRecurringTransactionId = 12;
            final int _cursorIndexOfDebtId = 13;
            final int _cursorIndexOfSavingsGoalId = 14;
            final int _cursorIndexOfIsReconciled = 15;
            final int _cursorIndexOfCreatedAt = 16;
            final int _cursorIndexOfUpdatedAt = 17;
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TransactionWithDetails>> getRecentTransactionsWithDetailsFlow(final int limit) {
    final String _sql = "SELECT * FROM transactions ORDER BY transactionDate DESC, id DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TransactionWithDetails>> getTransactionsBetweenDatesFlow(final long startDate,
      final long endDate) {
    final String _sql = "SELECT * FROM transactions WHERE transactionDate BETWEEN ? AND ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTransactionsBetweenDatesDirect(final long startDate, final long endDate,
      final Continuation<? super List<TransactionWithDetails>> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE transactionDate BETWEEN ? AND ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionWithDetails>> getTransactionsForAccountFlow(final long accountId) {
    final String _sql = "SELECT * FROM transactions WHERE accountId = ? OR toAccountId = ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, accountId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TransactionWithDetails>> getTransactionsForCategoryFlow(final long categoryId) {
    final String _sql = "SELECT * FROM transactions WHERE categoryId = ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTransactionWithDetailsById(final long id,
      final Continuation<? super TransactionWithDetails> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<TransactionWithDetails>() {
      @Override
      @Nullable
      public TransactionWithDetails call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final TransactionWithDetails _result;
            if (_cursor.moveToFirst()) {
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _result = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<TransactionWithDetails> getTransactionWithDetailsByIdFlow(final long id) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<TransactionWithDetails>() {
      @Override
      @Nullable
      public TransactionWithDetails call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final TransactionWithDetails _result;
            if (_cursor.moveToFirst()) {
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _result = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTransactionEntityById(final long id,
      final Continuation<? super TransactionEntity> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
          final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
          final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
          final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
          final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final TransactionType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_1;
            }
            final BigDecimal _tmpAmount;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfAmount)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpAmount = _tmp_3;
            }
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final long _tmpAccountId;
            _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            final Long _tmpToAccountId;
            if (_cursor.isNull(_cursorIndexOfToAccountId)) {
              _tmpToAccountId = null;
            } else {
              _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
            }
            final PaymentMethod _tmpPaymentMethod;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
            } else {
              _tmpPaymentMethod = _tmp_5;
            }
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpReceiptUri;
            if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
              _tmpReceiptUri = null;
            } else {
              _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
            }
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final BigDecimal _tmpExchangeRate;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
            }
            final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpExchangeRate = _tmp_7;
            }
            final Long _tmpRecurringTransactionId;
            if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
              _tmpRecurringTransactionId = null;
            } else {
              _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
            }
            final Long _tmpDebtId;
            if (_cursor.isNull(_cursorIndexOfDebtId)) {
              _tmpDebtId = null;
            } else {
              _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
            }
            final Long _tmpSavingsGoalId;
            if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
              _tmpSavingsGoalId = null;
            } else {
              _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
            }
            final boolean _tmpIsReconciled;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
            _tmpIsReconciled = _tmp_8 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionWithDetails>> searchTransactionsFlow(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE (notes LIKE '%' || ? || '%' OR amount LIKE '%' || ? || '%')\n"
            + "        ORDER BY transactionDate DESC, id DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp = null;
              } else {
                _tmp = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_1 = __roomTypeConverters.toTransactionType(_tmp);
              if (_tmp_1 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_1;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_3;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_5 = __roomTypeConverters.toPaymentMethod(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_5;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_7;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_8;
              _tmp_8 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_8 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TransactionWithDetails>> getFilteredTransactionsFlow(final TransactionType type,
      final Long accountId, final Long categoryId, final long startDate, final long endDate) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE (? IS NULL OR type = ?)\n"
            + "        AND (? IS NULL OR accountId = ? OR toAccountId = ?)\n"
            + "        AND (? IS NULL OR categoryId = ?)\n"
            + "        AND (transactionDate BETWEEN ? AND ?)\n"
            + "        ORDER BY transactionDate DESC, id DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 9);
    int _argIndex = 1;
    final String _tmp = __roomTypeConverters.fromTransactionType(type);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __roomTypeConverters.fromTransactionType(type);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    _argIndex = 3;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 4;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 5;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 6;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, categoryId);
    }
    _argIndex = 7;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, categoryId);
    }
    _argIndex = 8;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 9;
    _statement.bindLong(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "accounts",
        "transactions"}, new Callable<List<TransactionWithDetails>>() {
      @Override
      @NonNull
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
            final int _cursorIndexOfToAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "toAccountId");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfReceiptUri = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptUri");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfExchangeRate = CursorUtil.getColumnIndexOrThrow(_cursor, "exchangeRate");
            final int _cursorIndexOfRecurringTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringTransactionId");
            final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
            final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
            final int _cursorIndexOfIsReconciled = CursorUtil.getColumnIndexOrThrow(_cursor, "isReconciled");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<AccountEntity> _collectionAccount = new LongSparseArray<AccountEntity>();
            final LongSparseArray<AccountEntity> _collectionToAccount = new LongSparseArray<AccountEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAccountId);
              _collectionAccount.put(_tmpKey_1, null);
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_2 != null) {
                _collectionToAccount.put(_tmpKey_2, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionAccount);
            __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(_collectionToAccount);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final TransactionEntity _tmpTransaction;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final TransactionType _tmpType;
              final String _tmp_2;
              if (_cursor.isNull(_cursorIndexOfType)) {
                _tmp_2 = null;
              } else {
                _tmp_2 = _cursor.getString(_cursorIndexOfType);
              }
              final TransactionType _tmp_3 = __roomTypeConverters.toTransactionType(_tmp_2);
              if (_tmp_3 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.TransactionType', but it was NULL.");
              } else {
                _tmpType = _tmp_3;
              }
              final BigDecimal _tmpAmount;
              final String _tmp_4;
              if (_cursor.isNull(_cursorIndexOfAmount)) {
                _tmp_4 = null;
              } else {
                _tmp_4 = _cursor.getString(_cursorIndexOfAmount);
              }
              final BigDecimal _tmp_5 = __roomTypeConverters.toBigDecimal(_tmp_4);
              if (_tmp_5 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpAmount = _tmp_5;
              }
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final long _tmpAccountId;
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
              final Long _tmpToAccountId;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpToAccountId = null;
              } else {
                _tmpToAccountId = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              final PaymentMethod _tmpPaymentMethod;
              final String _tmp_6;
              if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
                _tmp_6 = null;
              } else {
                _tmp_6 = _cursor.getString(_cursorIndexOfPaymentMethod);
              }
              final PaymentMethod _tmp_7 = __roomTypeConverters.toPaymentMethod(_tmp_6);
              if (_tmp_7 == null) {
                throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.PaymentMethod', but it was NULL.");
              } else {
                _tmpPaymentMethod = _tmp_7;
              }
              final long _tmpTransactionDate;
              _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final String _tmpReceiptUri;
              if (_cursor.isNull(_cursorIndexOfReceiptUri)) {
                _tmpReceiptUri = null;
              } else {
                _tmpReceiptUri = _cursor.getString(_cursorIndexOfReceiptUri);
              }
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final BigDecimal _tmpExchangeRate;
              final String _tmp_8;
              if (_cursor.isNull(_cursorIndexOfExchangeRate)) {
                _tmp_8 = null;
              } else {
                _tmp_8 = _cursor.getString(_cursorIndexOfExchangeRate);
              }
              final BigDecimal _tmp_9 = __roomTypeConverters.toBigDecimal(_tmp_8);
              if (_tmp_9 == null) {
                throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
              } else {
                _tmpExchangeRate = _tmp_9;
              }
              final Long _tmpRecurringTransactionId;
              if (_cursor.isNull(_cursorIndexOfRecurringTransactionId)) {
                _tmpRecurringTransactionId = null;
              } else {
                _tmpRecurringTransactionId = _cursor.getLong(_cursorIndexOfRecurringTransactionId);
              }
              final Long _tmpDebtId;
              if (_cursor.isNull(_cursorIndexOfDebtId)) {
                _tmpDebtId = null;
              } else {
                _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
              }
              final Long _tmpSavingsGoalId;
              if (_cursor.isNull(_cursorIndexOfSavingsGoalId)) {
                _tmpSavingsGoalId = null;
              } else {
                _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
              }
              final boolean _tmpIsReconciled;
              final int _tmp_10;
              _tmp_10 = _cursor.getInt(_cursorIndexOfIsReconciled);
              _tmpIsReconciled = _tmp_10 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpTransaction = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpToAccountId,_tmpPaymentMethod,_tmpTransactionDate,_tmpNotes,_tmpReceiptUri,_tmpCurrency,_tmpExchangeRate,_tmpRecurringTransactionId,_tmpDebtId,_tmpSavingsGoalId,_tmpIsReconciled,_tmpCreatedAt,_tmpUpdatedAt);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_3 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_3);
              } else {
                _tmpCategory = null;
              }
              final AccountEntity _tmpAccount;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfAccountId);
              _tmpAccount = _collectionAccount.get(_tmpKey_4);
              final AccountEntity _tmpToAccount;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfToAccountId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfToAccountId);
              }
              if (_tmpKey_5 != null) {
                _tmpToAccount = _collectionToAccount.get(_tmpKey_5);
              } else {
                _tmpToAccount = null;
              }
              _item = new TransactionWithDetails(_tmpTransaction,_tmpCategory,_tmpAccount,_tmpToAccount);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTransactionsCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM transactions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(
      @NonNull final LongSparseArray<CategoryEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomFintrackAndroidDataLocalEntityCategoryEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`type`,`iconName`,`colorHex`,`parentCategoryId`,`isDefault`,`isFavorite`,`createdAt` FROM `categories` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfType = 2;
      final int _cursorIndexOfIconName = 3;
      final int _cursorIndexOfColorHex = 4;
      final int _cursorIndexOfParentCategoryId = 5;
      final int _cursorIndexOfIsDefault = 6;
      final int _cursorIndexOfIsFavorite = 7;
      final int _cursorIndexOfCreatedAt = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final CategoryEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final CategoryType _tmpType;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfType)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfType);
          }
          final CategoryType _tmp_1 = __roomTypeConverters.toCategoryType(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.CategoryType', but it was NULL.");
          } else {
            _tmpType = _tmp_1;
          }
          final String _tmpIconName;
          _tmpIconName = _cursor.getString(_cursorIndexOfIconName);
          final String _tmpColorHex;
          _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
          final Long _tmpParentCategoryId;
          if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
            _tmpParentCategoryId = null;
          } else {
            _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
          }
          final boolean _tmpIsDefault;
          final int _tmp_2;
          _tmp_2 = _cursor.getInt(_cursorIndexOfIsDefault);
          _tmpIsDefault = _tmp_2 != 0;
          final boolean _tmpIsFavorite;
          final int _tmp_3;
          _tmp_3 = _cursor.getInt(_cursorIndexOfIsFavorite);
          _tmpIsFavorite = _tmp_3 != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          _item_1 = new CategoryEntity(_tmpId,_tmpName,_tmpType,_tmpIconName,_tmpColorHex,_tmpParentCategoryId,_tmpIsDefault,_tmpIsFavorite,_tmpCreatedAt);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(
      @NonNull final LongSparseArray<AccountEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipaccountsAscomFintrackAndroidDataLocalEntityAccountEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`type`,`openingBalance`,`currentBalance`,`currency`,`description`,`isArchived`,`createdAt`,`updatedAt` FROM `accounts` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfType = 2;
      final int _cursorIndexOfOpeningBalance = 3;
      final int _cursorIndexOfCurrentBalance = 4;
      final int _cursorIndexOfCurrency = 5;
      final int _cursorIndexOfDescription = 6;
      final int _cursorIndexOfIsArchived = 7;
      final int _cursorIndexOfCreatedAt = 8;
      final int _cursorIndexOfUpdatedAt = 9;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final AccountEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final AccountType _tmpType;
          final String _tmp;
          if (_cursor.isNull(_cursorIndexOfType)) {
            _tmp = null;
          } else {
            _tmp = _cursor.getString(_cursorIndexOfType);
          }
          final AccountType _tmp_1 = __roomTypeConverters.toAccountType(_tmp);
          if (_tmp_1 == null) {
            throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.AccountType', but it was NULL.");
          } else {
            _tmpType = _tmp_1;
          }
          final BigDecimal _tmpOpeningBalance;
          final String _tmp_2;
          if (_cursor.isNull(_cursorIndexOfOpeningBalance)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = _cursor.getString(_cursorIndexOfOpeningBalance);
          }
          final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
          if (_tmp_3 == null) {
            throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
          } else {
            _tmpOpeningBalance = _tmp_3;
          }
          final BigDecimal _tmpCurrentBalance;
          final String _tmp_4;
          if (_cursor.isNull(_cursorIndexOfCurrentBalance)) {
            _tmp_4 = null;
          } else {
            _tmp_4 = _cursor.getString(_cursorIndexOfCurrentBalance);
          }
          final BigDecimal _tmp_5 = __roomTypeConverters.toBigDecimal(_tmp_4);
          if (_tmp_5 == null) {
            throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
          } else {
            _tmpCurrentBalance = _tmp_5;
          }
          final String _tmpCurrency;
          _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
          final String _tmpDescription;
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
          final boolean _tmpIsArchived;
          final int _tmp_6;
          _tmp_6 = _cursor.getInt(_cursorIndexOfIsArchived);
          _tmpIsArchived = _tmp_6 != 0;
          final long _tmpCreatedAt;
          _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
          final long _tmpUpdatedAt;
          _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
          _item_1 = new AccountEntity(_tmpId,_tmpName,_tmpType,_tmpOpeningBalance,_tmpCurrentBalance,_tmpCurrency,_tmpDescription,_tmpIsArchived,_tmpCreatedAt,_tmpUpdatedAt);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
