package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.CustomApp;

public class DataSource {
    private static DataSource sInstance;

    private SQLiteDatabase mDatabase;

    private DataSource() {
        mDatabase = new DbHelper(CustomApp.getContext()).getWritableDatabase();
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
        values.put(DbSchema.LogTable.Cols.CODE, log.getCode());
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

        String query = "SELECT * FROM " + DbSchema.LogTable.NAME +
                " ORDER BY " + DbSchema.LogTable.Cols.DATE + " DESC " +
                " LIMIT 30";

        List<Log> logs = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(query, null);
        LogCursorWrapper logCursor = new LogCursorWrapper(cursor);
        try {
            if (logCursor != null && logCursor.moveToFirst()) {
                while (!logCursor.isAfterLast()) {
                    logs.add(logCursor.getLog());
                    logCursor.moveToNext();
                }
            }
        } finally {
            if (logCursor != null) {
                logCursor.close();
            }
        }

        return logs;
    }

    public Log getLastLog(String code) {

        String query = "SELECT * FROM " + DbSchema.LogTable.NAME +
                " WHERE " + DbSchema.LogTable.Cols.CODE + " = ? " +
                " ORDER BY " + DbSchema.LogTable.Cols.DATE + " DESC " +
                " LIMIT 1";
        String[] args = {code};

        Cursor cursor = mDatabase.rawQuery(query, args);
        LogCursorWrapper logCursor = new LogCursorWrapper(cursor);
        try {
            if (cursor != null && logCursor.moveToFirst()) {
                return logCursor.getLog();
            }
        } finally {
            if (logCursor != null) {
                logCursor.close();
            }
        }
        return null;
    }

}
