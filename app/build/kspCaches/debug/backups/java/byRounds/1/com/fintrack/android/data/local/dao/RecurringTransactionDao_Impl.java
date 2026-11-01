package com.fintrack.android.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fintrack.android.data.local.converters.RoomTypeConverters;
import com.fintrack.android.data.local.entity.RecurringTransactionEntity;
import com.fintrack.android.domain.model.PaymentMethod;
import com.fintrack.android.domain.model.RecurrenceFrequency;
import com.fintrack.android.domain.model.TransactionType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class RecurringTransactionDao_Impl implements RecurringTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecurringTransactionEntity> __insertionAdapterOfRecurringTransactionEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __deletionAdapterOfRecurringTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __updateAdapterOfRecurringTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateProcessedSchedule;

  public RecurringTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecurringTransactionEntity = new EntityInsertionAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recurring_transactions` (`id`,`title`,`type`,`amount`,`categoryId`,`accountId`,`paymentMethod`,`frequency`,`intervalValue`,`startDate`,`endDate`,`nextDueDate`,`lastProcessedDate`,`isActive`,`autoCreate`,`notes`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        final String _tmp = __roomTypeConverters.fromTransactionType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindLong(6, entity.getAccountId());
        final String _tmp_2 = __roomTypeConverters.fromPaymentMethod(entity.getPaymentMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        final String _tmp_3 = __roomTypeConverters.fromRecurrenceFrequency(entity.getFrequency());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        statement.bindLong(9, entity.getIntervalValue());
        statement.bindLong(10, entity.getStartDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getEndDate());
        }
        statement.bindLong(12, entity.getNextDueDate());
        if (entity.getLastProcessedDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastProcessedDate());
        }
        final int _tmp_4 = entity.isActive() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.getAutoCreate() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        statement.bindString(16, entity.getNotes());
        statement.bindLong(17, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recurring_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recurring_transactions` SET `id` = ?,`title` = ?,`type` = ?,`amount` = ?,`categoryId` = ?,`accountId` = ?,`paymentMethod` = ?,`frequency` = ?,`intervalValue` = ?,`startDate` = ?,`endDate` = ?,`nextDueDate` = ?,`lastProcessedDate` = ?,`isActive` = ?,`autoCreate` = ?,`notes` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        final String _tmp = __roomTypeConverters.fromTransactionType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindLong(6, entity.getAccountId());
        final String _tmp_2 = __roomTypeConverters.fromPaymentMethod(entity.getPaymentMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        final String _tmp_3 = __roomTypeConverters.fromRecurrenceFrequency(entity.getFrequency());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        statement.bindLong(9, entity.getIntervalValue());
        statement.bindLong(10, entity.getStartDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getEndDate());
        }
        statement.bindLong(12, entity.getNextDueDate());
        if (entity.getLastProcessedDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastProcessedDate());
        }
        final int _tmp_4 = entity.isActive() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.getAutoCreate() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        statement.bindString(16, entity.getNotes());
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateProcessedSchedule = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE recurring_transactions SET lastProcessedDate = ?, nextDueDate = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecurringTransaction(final RecurringTransactionEntity recurring,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRecurringTransactionEntity.insertAndReturnId(recurring);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecurringTransaction(final RecurringTransactionEntity recurring,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecurringTransactionEntity.handle(recurring);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRecurringTransaction(final RecurringTransactionEntity recurring,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecurringTransactionEntity.handle(recurring);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProcessedSchedule(final long id, final long lastProcessed, final long nextDue,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateProcessedSchedule.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lastProcessed);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, nextDue);
        _argIndex = 3;
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
          __preparedStmtOfUpdateProcessedSchedule.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RecurringTransactionEntity>> getAllRecurringTransactionsFlow() {
    final String _sql = "SELECT `recurring_transactions`.`id` AS `id`, `recurring_transactions`.`title` AS `title`, `recurring_transactions`.`type` AS `type`, `recurring_transactions`.`amount` AS `amount`, `recurring_transactions`.`categoryId` AS `categoryId`, `recurring_transactions`.`accountId` AS `accountId`, `recurring_transactions`.`paymentMethod` AS `paymentMethod`, `recurring_transactions`.`frequency` AS `frequency`, `recurring_transactions`.`intervalValue` AS `intervalValue`, `recurring_transactions`.`startDate` AS `startDate`, `recurring_transactions`.`endDate` AS `endDate`, `recurring_transactions`.`nextDueDate` AS `nextDueDate`, `recurring_transactions`.`lastProcessedDate` AS `lastProcessedDate`, `recurring_transactions`.`isActive` AS `isActive`, `recurring_transactions`.`autoCreate` AS `autoCreate`, `recurring_transactions`.`notes` AS `notes`, `recurring_transactions`.`createdAt` AS `createdAt` FROM recurring_transactions ORDER BY nextDueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recurring_transactions"}, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfTitle = 1;
          final int _cursorIndexOfType = 2;
          final int _cursorIndexOfAmount = 3;
          final int _cursorIndexOfCategoryId = 4;
          final int _cursorIndexOfAccountId = 5;
          final int _cursorIndexOfPaymentMethod = 6;
          final int _cursorIndexOfFrequency = 7;
          final int _cursorIndexOfIntervalValue = 8;
          final int _cursorIndexOfStartDate = 9;
          final int _cursorIndexOfEndDate = 10;
          final int _cursorIndexOfNextDueDate = 11;
          final int _cursorIndexOfLastProcessedDate = 12;
          final int _cursorIndexOfIsActive = 13;
          final int _cursorIndexOfAutoCreate = 14;
          final int _cursorIndexOfNotes = 15;
          final int _cursorIndexOfCreatedAt = 16;
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final RecurrenceFrequency _tmpFrequency;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfFrequency);
            }
            final RecurrenceFrequency _tmp_7 = __roomTypeConverters.toRecurrenceFrequency(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.RecurrenceFrequency', but it was NULL.");
            } else {
              _tmpFrequency = _tmp_7;
            }
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final long _tmpNextDueDate;
            _tmpNextDueDate = _cursor.getLong(_cursorIndexOfNextDueDate);
            final Long _tmpLastProcessedDate;
            if (_cursor.isNull(_cursorIndexOfLastProcessedDate)) {
              _tmpLastProcessedDate = null;
            } else {
              _tmpLastProcessedDate = _cursor.getLong(_cursorIndexOfLastProcessedDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_8 != 0;
            final boolean _tmpAutoCreate;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfAutoCreate);
            _tmpAutoCreate = _tmp_9 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new RecurringTransactionEntity(_tmpId,_tmpTitle,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpPaymentMethod,_tmpFrequency,_tmpIntervalValue,_tmpStartDate,_tmpEndDate,_tmpNextDueDate,_tmpLastProcessedDate,_tmpIsActive,_tmpAutoCreate,_tmpNotes,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDueRecurringTransactions(final long timestamp,
      final Continuation<? super List<RecurringTransactionEntity>> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextDueDate <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, timestamp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNextDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextDueDate");
          final int _cursorIndexOfLastProcessedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProcessedDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfAutoCreate = CursorUtil.getColumnIndexOrThrow(_cursor, "autoCreate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final RecurrenceFrequency _tmpFrequency;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfFrequency);
            }
            final RecurrenceFrequency _tmp_7 = __roomTypeConverters.toRecurrenceFrequency(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.RecurrenceFrequency', but it was NULL.");
            } else {
              _tmpFrequency = _tmp_7;
            }
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final long _tmpNextDueDate;
            _tmpNextDueDate = _cursor.getLong(_cursorIndexOfNextDueDate);
            final Long _tmpLastProcessedDate;
            if (_cursor.isNull(_cursorIndexOfLastProcessedDate)) {
              _tmpLastProcessedDate = null;
            } else {
              _tmpLastProcessedDate = _cursor.getLong(_cursorIndexOfLastProcessedDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_8 != 0;
            final boolean _tmpAutoCreate;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfAutoCreate);
            _tmpAutoCreate = _tmp_9 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new RecurringTransactionEntity(_tmpId,_tmpTitle,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpPaymentMethod,_tmpFrequency,_tmpIntervalValue,_tmpStartDate,_tmpEndDate,_tmpNextDueDate,_tmpLastProcessedDate,_tmpIsActive,_tmpAutoCreate,_tmpNotes,_tmpCreatedAt);
            _result.add(_item);
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
  public Object getRecurringTransactionById(final long id,
      final Continuation<? super RecurringTransactionEntity> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RecurringTransactionEntity>() {
      @Override
      @Nullable
      public RecurringTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNextDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextDueDate");
          final int _cursorIndexOfLastProcessedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProcessedDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfAutoCreate = CursorUtil.getColumnIndexOrThrow(_cursor, "autoCreate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final RecurringTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final RecurrenceFrequency _tmpFrequency;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfFrequency);
            }
            final RecurrenceFrequency _tmp_7 = __roomTypeConverters.toRecurrenceFrequency(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.RecurrenceFrequency', but it was NULL.");
            } else {
              _tmpFrequency = _tmp_7;
            }
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final long _tmpNextDueDate;
            _tmpNextDueDate = _cursor.getLong(_cursorIndexOfNextDueDate);
            final Long _tmpLastProcessedDate;
            if (_cursor.isNull(_cursorIndexOfLastProcessedDate)) {
              _tmpLastProcessedDate = null;
            } else {
              _tmpLastProcessedDate = _cursor.getLong(_cursorIndexOfLastProcessedDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_8 != 0;
            final boolean _tmpAutoCreate;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfAutoCreate);
            _tmpAutoCreate = _tmp_9 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new RecurringTransactionEntity(_tmpId,_tmpTitle,_tmpType,_tmpAmount,_tmpCategoryId,_tmpAccountId,_tmpPaymentMethod,_tmpFrequency,_tmpIntervalValue,_tmpStartDate,_tmpEndDate,_tmpNextDueDate,_tmpLastProcessedDate,_tmpIsActive,_tmpAutoCreate,_tmpNotes,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
