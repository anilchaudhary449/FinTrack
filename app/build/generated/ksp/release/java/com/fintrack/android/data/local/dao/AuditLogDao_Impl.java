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
import com.fintrack.android.data.local.entity.AuditLogEntity;
import com.fintrack.android.domain.model.AuditAction;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AuditLogDao_Impl implements AuditLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AuditLogEntity> __insertionAdapterOfAuditLogEntity;

  private final RoomTypeConverters __roomTypeConverters = new RoomTypeConverters();

  public AuditLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAuditLogEntity = new EntityInsertionAdapter<AuditLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `audit_logs` (`id`,`entityType`,`entityId`,`action`,`description`,`previousStateJson`,`newStateJson`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AuditLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getEntityType());
        statement.bindLong(3, entity.getEntityId());
        final String _tmp = __roomTypeConverters.fromAuditAction(entity.getAction());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindString(5, entity.getDescription());
        if (entity.getPreviousStateJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPreviousStateJson());
        }
        if (entity.getNewStateJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNewStateJson());
        }
        statement.bindLong(8, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertAuditLog(final AuditLogEntity log,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAuditLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AuditLogEntity>> getRecentAuditLogsFlow(final int limit) {
    final String _sql = "SELECT * FROM audit_logs ORDER BY timestamp DESC, id DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audit_logs"}, new Callable<List<AuditLogEntity>>() {
      @Override
      @NonNull
      public List<AuditLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entityType");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entityId");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPreviousStateJson = CursorUtil.getColumnIndexOrThrow(_cursor, "previousStateJson");
          final int _cursorIndexOfNewStateJson = CursorUtil.getColumnIndexOrThrow(_cursor, "newStateJson");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AuditLogEntity> _result = new ArrayList<AuditLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AuditLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final long _tmpEntityId;
            _tmpEntityId = _cursor.getLong(_cursorIndexOfEntityId);
            final AuditAction _tmpAction;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAction);
            }
            final AuditAction _tmp_1 = __roomTypeConverters.toAuditAction(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.AuditAction', but it was NULL.");
            } else {
              _tmpAction = _tmp_1;
            }
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpPreviousStateJson;
            if (_cursor.isNull(_cursorIndexOfPreviousStateJson)) {
              _tmpPreviousStateJson = null;
            } else {
              _tmpPreviousStateJson = _cursor.getString(_cursorIndexOfPreviousStateJson);
            }
            final String _tmpNewStateJson;
            if (_cursor.isNull(_cursorIndexOfNewStateJson)) {
              _tmpNewStateJson = null;
            } else {
              _tmpNewStateJson = _cursor.getString(_cursorIndexOfNewStateJson);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AuditLogEntity(_tmpId,_tmpEntityType,_tmpEntityId,_tmpAction,_tmpDescription,_tmpPreviousStateJson,_tmpNewStateJson,_tmpTimestamp);
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
  public Flow<List<AuditLogEntity>> getAuditLogsForEntityFlow(final String entityType,
      final long entityId) {
    final String _sql = "SELECT * FROM audit_logs WHERE entityType = ? AND entityId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, entityType);
    _argIndex = 2;
    _statement.bindLong(_argIndex, entityId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audit_logs"}, new Callable<List<AuditLogEntity>>() {
      @Override
      @NonNull
      public List<AuditLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entityType");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entityId");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPreviousStateJson = CursorUtil.getColumnIndexOrThrow(_cursor, "previousStateJson");
          final int _cursorIndexOfNewStateJson = CursorUtil.getColumnIndexOrThrow(_cursor, "newStateJson");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AuditLogEntity> _result = new ArrayList<AuditLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AuditLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final long _tmpEntityId;
            _tmpEntityId = _cursor.getLong(_cursorIndexOfEntityId);
            final AuditAction _tmpAction;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAction);
            }
            final AuditAction _tmp_1 = __roomTypeConverters.toAuditAction(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.fintrack.android.domain.model.AuditAction', but it was NULL.");
            } else {
              _tmpAction = _tmp_1;
            }
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpPreviousStateJson;
            if (_cursor.isNull(_cursorIndexOfPreviousStateJson)) {
              _tmpPreviousStateJson = null;
            } else {
              _tmpPreviousStateJson = _cursor.getString(_cursorIndexOfPreviousStateJson);
            }
            final String _tmpNewStateJson;
            if (_cursor.isNull(_cursorIndexOfNewStateJson)) {
              _tmpNewStateJson = null;
            } else {
              _tmpNewStateJson = _cursor.getString(_cursorIndexOfNewStateJson);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AuditLogEntity(_tmpId,_tmpEntityType,_tmpEntityId,_tmpAction,_tmpDescription,_tmpPreviousStateJson,_tmpNewStateJson,_tmpTimestamp);
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
