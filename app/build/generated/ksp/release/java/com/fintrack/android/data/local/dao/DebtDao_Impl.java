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
import com.fintrack.android.data.local.entity.DebtEntity;
import com.fintrack.android.data.local.entity.DebtPaymentEntity;
import com.fintrack.android.domain.model.DebtStatus;
import com.fintrack.android.domain.model.DebtType;
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
public final class DebtDao_Impl implements DebtDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DebtEntity> __insertionAdapterOfDebtEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  private final EntityInsertionAdapter<DebtPaymentEntity> __insertionAdapterOfDebtPaymentEntity;

  private final EntityDeletionOrUpdateAdapter<DebtEntity> __deletionAdapterOfDebtEntity;

  private final EntityDeletionOrUpdateAdapter<DebtEntity> __updateAdapterOfDebtEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDebtOutstanding;

  public DebtDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDebtEntity = new EntityInsertionAdapter<DebtEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `debts` (`id`,`personName`,`type`,`originalAmount`,`outstandingAmount`,`interestRate`,`dueDate`,`status`,`notes`,`contactNumber`,`accountId`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DebtEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPersonName());
        final String _tmp = __roomTypeConverters.fromDebtType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getOriginalAmount());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = __roomTypeConverters.fromBigDecimal(entity.getOutstandingAmount());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = __roomTypeConverters.fromBigDecimal(entity.getInterestRate());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        if (entity.getDueDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDueDate());
        }
        final String _tmp_4 = __roomTypeConverters.fromDebtStatus(entity.getStatus());
        if (_tmp_4 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_4);
        }
        statement.bindString(9, entity.getNotes());
        statement.bindString(10, entity.getContactNumber());
        if (entity.getAccountId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getAccountId());
        }
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfDebtPaymentEntity = new EntityInsertionAdapter<DebtPaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `debt_payments` (`id`,`debtId`,`accountId`,`amount`,`paymentDate`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DebtPaymentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDebtId());
        statement.bindLong(3, entity.getAccountId());
        final String _tmp = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getPaymentDate());
        statement.bindString(6, entity.getNotes());
      }
    };
    this.__deletionAdapterOfDebtEntity = new EntityDeletionOrUpdateAdapter<DebtEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `debts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DebtEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDebtEntity = new EntityDeletionOrUpdateAdapter<DebtEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `debts` SET `id` = ?,`personName` = ?,`type` = ?,`originalAmount` = ?,`outstandingAmount` = ?,`interestRate` = ?,`dueDate` = ?,`status` = ?,`notes` = ?,`contactNumber` = ?,`accountId` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DebtEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPersonName());
        final String _tmp = __roomTypeConverters.fromDebtType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getOriginalAmount());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = __roomTypeConverters.fromBigDecimal(entity.getOutstandingAmount());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = __roomTypeConverters.fromBigDecimal(entity.getInterestRate());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        if (entity.getDueDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDueDate());
        }
        final String _tmp_4 = __roomTypeConverters.fromDebtStatus(entity.getStatus());
        if (_tmp_4 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_4);
        }
        statement.bindString(9, entity.getNotes());
        statement.bindString(10, entity.getContactNumber());
        if (entity.getAccountId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getAccountId());
        }
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDebtOutstanding = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE debts SET outstandingAmount = ?, status = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertDebt(final DebtEntity debt, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDebtEntity.insertAndReturnId(debt);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPayment(final DebtPaymentEntity payment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDebtPaymentEntity.insertAndReturnId(payment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDebt(final DebtEntity debt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDebtEntity.handle(debt);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDebt(final DebtEntity debt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDebtEntity.handle(debt);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDebtOutstanding(final long debtId, final BigDecimal newOutstanding,
      final DebtStatus newStatus, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDebtOutstanding.acquire();
        int _argIndex = 1;
        final String _tmp = __roomTypeConverters.fromBigDecimal(newOutstanding);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        final String _tmp_1 = __roomTypeConverters.fromDebtStatus(newStatus);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp_1);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, debtId);
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
          __preparedStmtOfUpdateDebtOutstanding.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DebtEntity>> getAllDebtsFlow() {
    final String _sql = "SELECT `debts`.`id` AS `id`, `debts`.`personName` AS `personName`, `debts`.`type` AS `type`, `debts`.`originalAmount` AS `originalAmount`, `debts`.`outstandingAmount` AS `outstandingAmount`, `debts`.`interestRate` AS `interestRate`, `debts`.`dueDate` AS `dueDate`, `debts`.`status` AS `status`, `debts`.`notes` AS `notes`, `debts`.`contactNumber` AS `contactNumber`, `debts`.`accountId` AS `accountId`, `debts`.`createdAt` AS `createdAt`, `debts`.`updatedAt` AS `updatedAt` FROM debts ORDER BY status ASC, dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"debts"}, new Callable<List<DebtEntity>>() {
      @Override
      @NonNull
      public List<DebtEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfPersonName = 1;
          final int _cursorIndexOfType = 2;
          final int _cursorIndexOfOriginalAmount = 3;
          final int _cursorIndexOfOutstandingAmount = 4;
          final int _cursorIndexOfInterestRate = 5;
          final int _cursorIndexOfDueDate = 6;
          final int _cursorIndexOfStatus = 7;
          final int _cursorIndexOfNotes = 8;
          final int _cursorIndexOfContactNumber = 9;
          final int _cursorIndexOfAccountId = 10;
          final int _cursorIndexOfCreatedAt = 11;
          final int _cursorIndexOfUpdatedAt = 12;
          final List<DebtEntity> _result = new ArrayList<DebtEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DebtEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final DebtType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            final DebtType _tmp_1 = __roomTypeConverters.toDebtType(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtType', but it was NULL.");
            } else {
              _tmpType = _tmp_1;
            }
            final BigDecimal _tmpOriginalAmount;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfOriginalAmount)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfOriginalAmount);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOriginalAmount = _tmp_3;
            }
            final BigDecimal _tmpOutstandingAmount;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfOutstandingAmount)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfOutstandingAmount);
            }
            final BigDecimal _tmp_5 = __roomTypeConverters.toBigDecimal(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOutstandingAmount = _tmp_5;
            }
            final BigDecimal _tmpInterestRate;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfInterestRate)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfInterestRate);
            }
            final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpInterestRate = _tmp_7;
            }
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final DebtStatus _tmpStatus;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfStatus);
            }
            final DebtStatus _tmp_9 = __roomTypeConverters.toDebtStatus(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtStatus', but it was NULL.");
            } else {
              _tmpStatus = _tmp_9;
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpContactNumber;
            _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
            final Long _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DebtEntity(_tmpId,_tmpPersonName,_tmpType,_tmpOriginalAmount,_tmpOutstandingAmount,_tmpInterestRate,_tmpDueDate,_tmpStatus,_tmpNotes,_tmpContactNumber,_tmpAccountId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<DebtEntity>> getDebtsByTypeFlow(final DebtType type) {
    final String _sql = "SELECT * FROM debts WHERE type = ? ORDER BY status ASC, dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __roomTypeConverters.fromDebtType(type);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"debts"}, new Callable<List<DebtEntity>>() {
      @Override
      @NonNull
      public List<DebtEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfOriginalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "originalAmount");
          final int _cursorIndexOfOutstandingAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "outstandingAmount");
          final int _cursorIndexOfInterestRate = CursorUtil.getColumnIndexOrThrow(_cursor, "interestRate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contactNumber");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<DebtEntity> _result = new ArrayList<DebtEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DebtEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final DebtType _tmpType;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfType);
            }
            final DebtType _tmp_2 = __roomTypeConverters.toDebtType(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtType', but it was NULL.");
            } else {
              _tmpType = _tmp_2;
            }
            final BigDecimal _tmpOriginalAmount;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfOriginalAmount)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfOriginalAmount);
            }
            final BigDecimal _tmp_4 = __roomTypeConverters.toBigDecimal(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOriginalAmount = _tmp_4;
            }
            final BigDecimal _tmpOutstandingAmount;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfOutstandingAmount)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfOutstandingAmount);
            }
            final BigDecimal _tmp_6 = __roomTypeConverters.toBigDecimal(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOutstandingAmount = _tmp_6;
            }
            final BigDecimal _tmpInterestRate;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfInterestRate)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfInterestRate);
            }
            final BigDecimal _tmp_8 = __roomTypeConverters.toBigDecimal(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpInterestRate = _tmp_8;
            }
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final DebtStatus _tmpStatus;
            final String _tmp_9;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getString(_cursorIndexOfStatus);
            }
            final DebtStatus _tmp_10 = __roomTypeConverters.toDebtStatus(_tmp_9);
            if (_tmp_10 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtStatus', but it was NULL.");
            } else {
              _tmpStatus = _tmp_10;
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpContactNumber;
            _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
            final Long _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DebtEntity(_tmpId,_tmpPersonName,_tmpType,_tmpOriginalAmount,_tmpOutstandingAmount,_tmpInterestRate,_tmpDueDate,_tmpStatus,_tmpNotes,_tmpContactNumber,_tmpAccountId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getDebtById(final long id, final Continuation<? super DebtEntity> $completion) {
    final String _sql = "SELECT * FROM debts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DebtEntity>() {
      @Override
      @Nullable
      public DebtEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonName = CursorUtil.getColumnIndexOrThrow(_cursor, "personName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfOriginalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "originalAmount");
          final int _cursorIndexOfOutstandingAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "outstandingAmount");
          final int _cursorIndexOfInterestRate = CursorUtil.getColumnIndexOrThrow(_cursor, "interestRate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contactNumber");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final DebtEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPersonName;
            _tmpPersonName = _cursor.getString(_cursorIndexOfPersonName);
            final DebtType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            final DebtType _tmp_1 = __roomTypeConverters.toDebtType(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtType', but it was NULL.");
            } else {
              _tmpType = _tmp_1;
            }
            final BigDecimal _tmpOriginalAmount;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfOriginalAmount)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfOriginalAmount);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOriginalAmount = _tmp_3;
            }
            final BigDecimal _tmpOutstandingAmount;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfOutstandingAmount)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfOutstandingAmount);
            }
            final BigDecimal _tmp_5 = __roomTypeConverters.toBigDecimal(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpOutstandingAmount = _tmp_5;
            }
            final BigDecimal _tmpInterestRate;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfInterestRate)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfInterestRate);
            }
            final BigDecimal _tmp_7 = __roomTypeConverters.toBigDecimal(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpInterestRate = _tmp_7;
            }
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final DebtStatus _tmpStatus;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfStatus);
            }
            final DebtStatus _tmp_9 = __roomTypeConverters.toDebtStatus(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.DebtStatus', but it was NULL.");
            } else {
              _tmpStatus = _tmp_9;
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpContactNumber;
            _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
            final Long _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new DebtEntity(_tmpId,_tmpPersonName,_tmpType,_tmpOriginalAmount,_tmpOutstandingAmount,_tmpInterestRate,_tmpDueDate,_tmpStatus,_tmpNotes,_tmpContactNumber,_tmpAccountId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<DebtPaymentEntity>> getPaymentsForDebtFlow(final long debtId) {
    final String _sql = "SELECT * FROM debt_payments WHERE debtId = ? ORDER BY paymentDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, debtId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"debt_payments"}, new Callable<List<DebtPaymentEntity>>() {
      @Override
      @NonNull
      public List<DebtPaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDebtId = CursorUtil.getColumnIndexOrThrow(_cursor, "debtId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<DebtPaymentEntity> _result = new ArrayList<DebtPaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DebtPaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDebtId;
            _tmpDebtId = _cursor.getLong(_cursorIndexOfDebtId);
            final long _tmpAccountId;
            _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            final BigDecimal _tmpAmount;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAmount)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAmount);
            }
            final BigDecimal _tmp_1 = __roomTypeConverters.toBigDecimal(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpAmount = _tmp_1;
            }
            final long _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getLong(_cursorIndexOfPaymentDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new DebtPaymentEntity(_tmpId,_tmpDebtId,_tmpAccountId,_tmpAmount,_tmpPaymentDate,_tmpNotes);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
