package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.CustomApp;
import es.dicarea.postman.whereisthepostman.StatusItem;

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

    private static ContentValues getContentValues(StatusItem statusItem) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.StatusTable.Cols.ID, statusItem.getId());
        values.put(DbSchema.StatusTable.Cols.DATE, statusItem.getTime());
        values.put(DbSchema.StatusTable.Cols.STATUS, statusItem.getStatus().getOrder());
        values.put(DbSchema.StatusTable.Cols.CODE, statusItem.getCode());
        return values;
    }

    public void addStatus(StatusItem statusItem) {
        ContentValues values = getContentValues(statusItem);
        long id = mDatabase.insert(DbSchema.StatusTable.NAME, null, values);
        statusItem.setId(((int) id));
    }

    public void deleteStatus(StatusItem statusItem) {
        mDatabase.delete(DbSchema.StatusTable.NAME, DbSchema.StatusTable.Cols.ID + " = ?", new String[]{statusItem.getId().toString()});
    }

    private void updateStatus(StatusItem statusItem) {
        ContentValues values = getContentValues(statusItem);
        mDatabase.update(DbSchema.StatusTable.NAME, values, DbSchema.StatusTable.Cols.ID + " = " + statusItem.getId(), null);
    }

    public List<StatusItem> getStatusList() {

        String query = "SELECT * FROM " + DbSchema.StatusTable.NAME +
                " ORDER BY " + DbSchema.StatusTable.Cols.DATE + " DESC " +
                " LIMIT 30";

        List<StatusItem> statusItems = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(query, null);
        StatusCursorWrapper statusCursor = new StatusCursorWrapper(cursor);
        try {
            if (statusCursor != null && statusCursor.moveToFirst()) {
                while (!statusCursor.isAfterLast()) {
                    statusItems.add(statusCursor.getStatus());
                    statusCursor.moveToNext();
                }
            }
        } finally {
            if (statusCursor != null) {
                statusCursor.close();
            }
        }

        return statusItems;
    }

    public StatusItem getLastStatus(String code) {

        String query = "SELECT * FROM " + DbSchema.StatusTable.NAME +
                " WHERE " + DbSchema.StatusTable.Cols.CODE + " = ? " +
                " ORDER BY " + DbSchema.StatusTable.Cols.DATE + " DESC " +
                " LIMIT 1";
        String[] args = {code};

        Cursor cursor = mDatabase.rawQuery(query, args);
        StatusCursorWrapper statusCursor = new StatusCursorWrapper(cursor);
        try {
            if (cursor != null && statusCursor.moveToFirst()) {
                return statusCursor.getStatus();
            }
        } finally {
            if (statusCursor != null) {
                statusCursor.close();
            }
        }
        return null;
    }

}
