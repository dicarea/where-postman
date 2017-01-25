package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.CustomApp;
import es.dicarea.postman.whereisthepostman.StatusCorreosEnum;
import es.dicarea.postman.whereisthepostman.db.WrapperRepository.StatusCursorWrapper;
import es.dicarea.postman.whereisthepostman.db.WrapperRepository.TrackingCursorWrapper;

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
        values.put(DbSchema.StatusTable.Cols.TRACKING_ID, statusItem.getTracking().getId());
        values.put(DbSchema.StatusTable.Cols.DATE, statusItem.getTime());
        values.put(DbSchema.StatusTable.Cols.STATUS, statusItem.getStatus().getOrder());
        return values;
    }

    public void addStatus(StatusItem statusItem) {
        ContentValues values = getContentValues(statusItem);
        long id = mDatabase.insert(DbSchema.StatusTable.NAME, null, values);
        statusItem.setId(((int) id));
    }

    public List<StatusItem> getStatusList(Integer trackingId) {

        String query = "SELECT * FROM " + DbSchema.StatusTable.NAME +
                " WHERE " + DbSchema.StatusTable.Cols.TRACKING_ID + " = " + trackingId +
                " ORDER BY " + DbSchema.StatusTable.Cols.DATE + " DESC " +
                " LIMIT 30";

        Cursor cursor = mDatabase.rawQuery(query, null);

        List<StatusItem> statusItems = new ArrayList<>();
        StatusCursorWrapper statusCursor = new StatusCursorWrapper(cursor);
        try {
            if (statusCursor != null && statusCursor.moveToFirst()) {
                while (!statusCursor.isAfterLast()) {
                    statusItems.add(statusCursor.getElement());
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

    public StatusCorreosEnum getLastValidStatus(Integer trackingId) {

        String query = "SELECT " + DbSchema.StatusTable.Cols.STATUS +
                " FROM " + DbSchema.StatusTable.NAME +
                " WHERE " + DbSchema.StatusTable.Cols.TRACKING_ID + " = " + trackingId +
                " AND " + DbSchema.StatusTable.Cols.STATUS + " != 0 " +
                " ORDER BY " + DbSchema.StatusTable.Cols.DATE + " DESC " +
                " LIMIT 1 ";

        Cursor cursor = mDatabase.rawQuery(query, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                Integer status = cursor.getInt(cursor.getColumnIndex(DbSchema.StatusTable.Cols.STATUS));
                return StatusCorreosEnum.getStatus(status);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public void deleteStatusByTracking(Integer idTracking) {
        String strSQL = "DELETE FROM " + DbSchema.StatusTable.NAME + " " +
                " WHERE " + DbSchema.StatusTable.Cols.TRACKING_ID + " = " + idTracking;

        mDatabase.execSQL(strSQL);
    }

    //******************** TRACKING *******************

    public List<TrackingItem> getTrackingListActive() {

        String query = "SELECT * FROM " + DbSchema.TrackingTable.NAME +
                " WHERE " + DbSchema.TrackingTable.Cols.ACTIVE + " = 1 " +
                " AND " + DbSchema.TrackingTable.Cols.DELETED + " = 0 " +
                " ORDER BY " + DbSchema.TrackingTable.Cols.CODE + " ASC";

        List<TrackingItem> trackingItems = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(query, null);
        TrackingCursorWrapper trackingCursor = new TrackingCursorWrapper(cursor);
        try {
            if (trackingCursor != null && trackingCursor.moveToFirst()) {
                while (!trackingCursor.isAfterLast()) {
                    trackingItems.add(trackingCursor.getElement());
                    trackingCursor.moveToNext();
                }
            }
        } finally {
            if (trackingCursor != null) {
                trackingCursor.close();
            }
        }

        return trackingItems;
    }

    public List<TrackingItem> getTrackingList() {

        String query = "SELECT * FROM " + DbSchema.TrackingTable.NAME +
                " WHERE " + DbSchema.TrackingTable.Cols.DELETED + " = 0 " +
                " ORDER BY " + DbSchema.TrackingTable.Cols.CODE + " ASC";

        List<TrackingItem> trackingItems = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(query, null);
        TrackingCursorWrapper trackingCursor = new TrackingCursorWrapper(cursor);
        try {
            if (trackingCursor != null && trackingCursor.moveToFirst()) {
                while (!trackingCursor.isAfterLast()) {
                    trackingItems.add(trackingCursor.getElement());
                    trackingCursor.moveToNext();
                }
            }
        } finally {
            if (trackingCursor != null) {
                trackingCursor.close();
            }
        }

        return trackingItems;
    }

    public TrackingItem getTrackingById(Integer idTracking) {
        String query = "SELECT * FROM " + DbSchema.TrackingTable.NAME +
                " WHERE " + DbSchema.TrackingTable.Cols.ID + " = " + idTracking;

        Cursor cursor = mDatabase.rawQuery(query, null);
        TrackingCursorWrapper trackingCursor = new TrackingCursorWrapper(cursor);
        try {
            if (trackingCursor != null && trackingCursor.moveToFirst()) {

                return trackingCursor.getElement();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public TrackingItem getTrackingByCode(String code) {

        String query = "SELECT * FROM " + DbSchema.TrackingTable.NAME +
                " WHERE " + DbSchema.TrackingTable.Cols.CODE + " = '" + code + "'";

        Cursor cursor = mDatabase.rawQuery(query, null);
        TrackingCursorWrapper trackingCursor = new TrackingCursorWrapper(cursor);
        try {
            if (trackingCursor != null && trackingCursor.moveToFirst()) {

                return trackingCursor.getElement();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public void addTracking(TrackingItem trackingItem) {
        ContentValues values = getContentValues(trackingItem);
        long id = mDatabase.insert(DbSchema.TrackingTable.NAME, null, values);
        trackingItem.setId(((int) id));
    }

    public void updateTracking(TrackingItem trackingItem) {
        ContentValues values = getContentValues(trackingItem);
        mDatabase.update(DbSchema.TrackingTable.NAME, values, DbSchema.TrackingTable.Cols.ID + " =  " + trackingItem.getId(), null);
    }

    public void updateActiveTracking(TrackingItem trackingItem) {
        String strSQL = "UPDATE " + DbSchema.TrackingTable.NAME + " " +
                " SET " + DbSchema.TrackingTable.Cols.ACTIVE + " = " + (trackingItem.getActive() ? 1 : 0) +
                " WHERE " + DbSchema.TrackingTable.Cols.ID + " = " + trackingItem.getId();

        mDatabase.execSQL(strSQL);
    }

    public void deleteTracking(TrackingItem tracking) {
        String strSQL = "DELETE FROM " + DbSchema.TrackingTable.NAME + " " +
                " WHERE " + DbSchema.TrackingTable.Cols.ID + " = " + tracking.getId();

        mDatabase.execSQL(strSQL);
    }

    private static ContentValues getContentValues(TrackingItem trackingItem) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.TrackingTable.Cols.ID, trackingItem.getId());
        values.put(DbSchema.TrackingTable.Cols.CODE, trackingItem.getCode());
        values.put(DbSchema.TrackingTable.Cols.DESC, trackingItem.getDesc());
        values.put(DbSchema.TrackingTable.Cols.ACTIVE, true);
        values.put(DbSchema.TrackingTable.Cols.DELETED, false);
        return values;
    }

}
