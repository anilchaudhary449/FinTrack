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
import com.fintrack.android.data.local.entity.SavingsContributionEntity;
import com.fintrack.android.data.local.entity.SavingsGoalEntity;
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
public final class SavingsDao_Impl implements SavingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SavingsGoalEntity> __insertionAdapterOfSavingsGoalEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  private final EntityInsertionAdapter<SavingsContributionEntity> __insertionAdapterOfSavingsContributionEntity;

  private final EntityDeletionOrUpdateAdapter<SavingsGoalEntity> __deletionAdapterOfSavingsGoalEntity;

  private final EntityDeletionOrUpdateAdapter<SavingsGoalEntity> __updateAdapterOfSavingsGoalEntity;

  private final SharedSQLiteStatement __preparedStmtOfAddContributionAmount;

  public SavingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSavingsGoalEntity = new EntityInsertionAdapter<SavingsGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `savings_goals` (`id`,`title`,`description`,`targetAmount`,`currentSavedAmount`,`targetDate`,`isCompleted`,`iconName`,`colorHex`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavingsGoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __roomTypeConverters.fromBigDecimal(entity.getTargetAmount());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getCurrentSavedAmount());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        statement.bindLong(6, entity.getTargetDate());
        final int _tmp_2 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        statement.bindString(8, entity.getIconName());
        statement.bindString(9, entity.getColorHex());
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfSavingsContributionEntity = new EntityInsertionAdapter<SavingsContributionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `savings_contributions` (`id`,`savingsGoalId`,`accountId`,`amount`,`contributionDate`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavingsContributionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSavingsGoalId());
        statement.bindLong(3, entity.getAccountId());
        final String _tmp = __roomTypeConverters.fromBigDecimal(entity.getAmount());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getContributionDate());
        statement.bindString(6, entity.getNotes());
      }
    };
    this.__deletionAdapterOfSavingsGoalEntity = new EntityDeletionOrUpdateAdapter<SavingsGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `savings_goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavingsGoalEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSavingsGoalEntity = new EntityDeletionOrUpdateAdapter<SavingsGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `savings_goals` SET `id` = ?,`title` = ?,`description` = ?,`targetAmount` = ?,`currentSavedAmount` = ?,`targetDate` = ?,`isCompleted` = ?,`iconName` = ?,`colorHex` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavingsGoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __roomTypeConverters.fromBigDecimal(entity.getTargetAmount());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __roomTypeConverters.fromBigDecimal(entity.getCurrentSavedAmount());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        statement.bindLong(6, entity.getTargetDate());
        final int _tmp_2 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        statement.bindString(8, entity.getIconName());
        statement.bindString(9, entity.getColorHex());
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfAddContributionAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE savings_goals SET currentSavedAmount = currentSavedAmount + ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSavingsGoal(final SavingsGoalEntity goal,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSavingsGoalEntity.insertAndReturnId(goal);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertContribution(final SavingsContributionEntity contribution,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSavingsContributionEntity.insertAndReturnId(contribution);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSavingsGoal(final SavingsGoalEntity goal,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSavingsGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSavingsGoal(final SavingsGoalEntity goal,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSavingsGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addContributionAmount(final long goalId, final BigDecimal amount,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddContributionAmount.acquire();
        int _argIndex = 1;
        final String _tmp = __roomTypeConverters.fromBigDecimal(amount);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, goalId);
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
          __preparedStmtOfAddContributionAmount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SavingsGoalEntity>> getAllSavingsGoalsFlow() {
    final String _sql = "SELECT `savings_goals`.`id` AS `id`, `savings_goals`.`title` AS `title`, `savings_goals`.`description` AS `description`, `savings_goals`.`targetAmount` AS `targetAmount`, `savings_goals`.`currentSavedAmount` AS `currentSavedAmount`, `savings_goals`.`targetDate` AS `targetDate`, `savings_goals`.`isCompleted` AS `isCompleted`, `savings_goals`.`iconName` AS `iconName`, `savings_goals`.`colorHex` AS `colorHex`, `savings_goals`.`createdAt` AS `createdAt` FROM savings_goals ORDER BY isCompleted ASC, targetDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"savings_goals"}, new Callable<List<SavingsGoalEntity>>() {
      @Override
      @NonNull
      public List<SavingsGoalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfTitle = 1;
          final int _cursorIndexOfDescription = 2;
          final int _cursorIndexOfTargetAmount = 3;
          final int _cursorIndexOfCurrentSavedAmount = 4;
          final int _cursorIndexOfTargetDate = 5;
          final int _cursorIndexOfIsCompleted = 6;
          final int _cursorIndexOfIconName = 7;
          final int _cursorIndexOfColorHex = 8;
          final int _cursorIndexOfCreatedAt = 9;
          final List<SavingsGoalEntity> _result = new ArrayList<SavingsGoalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SavingsGoalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final BigDecimal _tmpTargetAmount;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTargetAmount)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTargetAmount);
            }
            final BigDecimal _tmp_1 = __roomTypeConverters.toBigDecimal(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpTargetAmount = _tmp_1;
            }
            final BigDecimal _tmpCurrentSavedAmount;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCurrentSavedAmount)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfCurrentSavedAmount);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpCurrentSavedAmount = _tmp_3;
            }
            final long _tmpTargetDate;
            _tmpTargetDate = _cursor.getLong(_cursorIndexOfTargetDate);
            final boolean _tmpIsCompleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_4 != 0;
            final String _tmpIconName;
            _tmpIconName = _cursor.getString(_cursorIndexOfIconName);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SavingsGoalEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpTargetAmount,_tmpCurrentSavedAmount,_tmpTargetDate,_tmpIsCompleted,_tmpIconName,_tmpColorHex,_tmpCreatedAt);
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
  public Object getSavingsGoalById(final long id,
      final Continuation<? super SavingsGoalEntity> $completion) {
    final String _sql = "SELECT * FROM savings_goals WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SavingsGoalEntity>() {
      @Override
      @Nullable
      public SavingsGoalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "targetAmount");
          final int _cursorIndexOfCurrentSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSavedAmount");
          final int _cursorIndexOfTargetDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDate");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIconName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconName");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final SavingsGoalEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final BigDecimal _tmpTargetAmount;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTargetAmount)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTargetAmount);
            }
            final BigDecimal _tmp_1 = __roomTypeConverters.toBigDecimal(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpTargetAmount = _tmp_1;
            }
            final BigDecimal _tmpCurrentSavedAmount;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCurrentSavedAmount)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfCurrentSavedAmount);
            }
            final BigDecimal _tmp_3 = __roomTypeConverters.toBigDecimal(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.");
            } else {
              _tmpCurrentSavedAmount = _tmp_3;
            }
            final long _tmpTargetDate;
            _tmpTargetDate = _cursor.getLong(_cursorIndexOfTargetDate);
            final boolean _tmpIsCompleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_4 != 0;
            final String _tmpIconName;
            _tmpIconName = _cursor.getString(_cursorIndexOfIconName);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new SavingsGoalEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpTargetAmount,_tmpCurrentSavedAmount,_tmpTargetDate,_tmpIsCompleted,_tmpIconName,_tmpColorHex,_tmpCreatedAt);
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
  public Flow<List<SavingsContributionEntity>> getContributionsForGoalFlow(final long goalId) {
    final String _sql = "SELECT * FROM savings_contributions WHERE savingsGoalId = ? ORDER BY contributionDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, goalId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"savings_contributions"}, new Callable<List<SavingsContributionEntity>>() {
      @Override
      @NonNull
      public List<SavingsContributionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSavingsGoalId = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsGoalId");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfContributionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "contributionDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<SavingsContributionEntity> _result = new ArrayList<SavingsContributionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SavingsContributionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSavingsGoalId;
            _tmpSavingsGoalId = _cursor.getLong(_cursorIndexOfSavingsGoalId);
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
            final long _tmpContributionDate;
            _tmpContributionDate = _cursor.getLong(_cursorIndexOfContributionDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new SavingsContributionEntity(_tmpId,_tmpSavingsGoalId,_tmpAccountId,_tmpAmount,_tmpContributionDate,_tmpNotes);
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
