package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.MyApp;

public class DataSource {
    private static DataSource sInstance;

    private SQLiteDatabase mDatabase;

    private DataSource() {
        mDatabase = new DbHelper(MyApp.getContext()).getWritableDatabase();
    }

    public static DataSource getInstance() {
        if (sInstance == null) {
            sInstance = new DataSource();
        }
        return sInstance;
    }

    private static ContentValues getContentValues(Log log) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.LogTable.Cols.ID, log.getId());
        values.put(DbSchema.LogTable.Cols.DATE, log.getDate());
        values.put(DbSchema.LogTable.Cols.STATUS, log.getStatus());
        return values;
    }

    public void addLog(Log log) {
        ContentValues values = getContentValues(log);
        long id = mDatabase.insert(DbSchema.LogTable.NAME, null, values);
        log.setId(((int) id));
    }

    public void deleteLog(Log log) {
        mDatabase.delete(DbSchema.LogTable.NAME, DbSchema.LogTable.Cols.ID + " = ?", new String[]{log.getId().toString()});
    }

    private void updateLog(Log log) {
        ContentValues values = getContentValues(log);
        mDatabase.update(DbSchema.LogTable.NAME, values, DbSchema.LogTable.Cols.ID + " = " + log.getId(), null);
    }

    public List<Log> getLogs() {
        List<Log> logs = new ArrayList<>();
        LogCursorWrapper cursor = queryLogs(null, null, DbSchema.LogTable.Cols.DATE + " DESC", "50");
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                logs.add(cursor.getLog());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return logs;
    }

    public Integer getLastStatus() {
        LogCursorWrapper cursor = queryLogs(null, null, DbSchema.LogTable.Cols.DATE + " DESC", "1");
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                return cursor.getLog().getStatus();
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private LogCursorWrapper queryLogs(String whereClause, String[] whereArgs, String orderBy, String limit) {
        Cursor cursor = mDatabase.query(
                DbSchema.LogTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                orderBy, // orderBy,
                limit // limit
        );
        return new LogCursorWrapper(cursor);
    }

}
