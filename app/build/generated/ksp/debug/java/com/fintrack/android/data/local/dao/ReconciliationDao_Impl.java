package com.fintrack.android.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fintrack.android.data.local.converters.RoomTypeConverters;
import com.fintrack.android.data.local.entity.ReconciliationRecordEntity;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReconciliationDao_Impl implements ReconciliationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReconciliationRecordEntity> __insertionAdapterOfReconciliationRecordEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  public ReconciliationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReconciliationRecordEntity = new EntityInsertionAdapter<ReconciliationRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reconciliation_records` (`id`,`accountId`,`reconciliationDate`,`appCalculatedBalance`,`actualStatementBalance`,`discrepancyAmount`,`adjustmentTransactionId`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReconciliationRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAccountId());
        statement.bindLong(3, entity.getReconciliationDate());
        final String _tmp = __roomTypeConverters.fromBigDecimal(entity.getAppCalculatedBalance());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getActualStatementBalance());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = __roomTypeConverters.fromBigDecimal(entity.getDiscrepancyAmount());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        if (entity.getAdjustmentTransactionId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getAdjustmentTransactionId());
        }
        statement.bindString(8, entity.getNotes());
      }
    };
  }

  @Override
  public Object insertReconciliationRecord(final ReconciliationRecordEntity record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReconciliationRecordEntity.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReconciliationRecordEntity>> getReconciliationsForAccountFlow(
      final long accountId) {
    final String _sql = "SELECT * FROM reconciliation_records WHERE accountId = ? ORDER BY reconciliationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, accountId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reconciliation_records"}, new Callable<List<ReconciliationRecordEntity>>() {
      @Override
      @NonNull
      public List<ReconciliationRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfReconciliationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reconciliationDate");
          final int _cursorIndexOfAppCalculatedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "appCalculatedBalance");
          final int _cursorIndexOfActualStatementBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "actualStatementBalance");
          final int _cursorIndexOfDiscrepancyAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "discrepancyAmount");
          final int _cursorIndexOfAdjustmentTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "adjustmentTransactionId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ReconciliationRecordEntity> _result = new ArrayList<ReconciliationRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReconciliationRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAccountId;
            _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            final long _tmpReconciliationDate;
            _tmpReconciliationDate = _cursor.getLong(_cursorIndexOfReconciliationDate);
            final BigDecimal _tmpAppCalculatedBalance;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAppCalculatedBalance)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAppCalculatedBalance);
            }
            final BigDecimal _tmp_1 = __roomTypeConverters.toBigDecimal(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpAppCalculatedBalance = _tmp_1;
            }
            final BigDecimal _tmpActualStatementBalance;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfActualStatementBalance)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfActualStatementBalance);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpActualStatementBalance = _tmp_3;
            }
            final BigDecimal _tmpDiscrepancyAmount;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDiscrepancyAmount)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDiscrepancyAmount);
            }
            final BigDecimal _tmp_5 = __roomTypeConverters.toBigDecimal(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpDiscrepancyAmount = _tmp_5;
            }
            final Long _tmpAdjustmentTransactionId;
            if (_cursor.isNull(_cursorIndexOfAdjustmentTransactionId)) {
              _tmpAdjustmentTransactionId = null;
            } else {
              _tmpAdjustmentTransactionId = _cursor.getLong(_cursorIndexOfAdjustmentTransactionId);
            }
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new ReconciliationRecordEntity(_tmpId,_tmpAccountId,_tmpReconciliationDate,_tmpAppCalculatedBalance,_tmpActualStatementBalance,_tmpDiscrepancyAmount,_tmpAdjustmentTransactionId,_tmpNotes);
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
