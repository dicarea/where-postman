package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.CustomApp;
import es.dicarea.postman.whereisthepostman.StatusEnum;
import es.dicarea.postman.whereisthepostman.db.WrapperRepository.CustomWrapper;
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

    public StatusEnum getMaxStatus(String code) {

        String query = "SELECT MAX( " + DbSchema.StatusTable.Cols.STATUS + " ) AS " + DbSchema.StatusTable.Cols.STATUS +
                " FROM " + DbSchema.StatusTable.NAME +
                " WHERE " + DbSchema.StatusTable.Cols.CODE + " = ? " +
                " ORDER BY " + DbSchema.StatusTable.Cols.DATE + " DESC " +
                " LIMIT 1";
        String[] args = {code};

        Cursor cursor = mDatabase.rawQuery(query, args);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                Integer status = cursor.getInt(cursor.getColumnIndex(DbSchema.StatusTable.Cols.STATUS));
                return StatusEnum.getStatus(status);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    //******************** TRACKING *******************

    public List<TrackingItem> getTrackingList() {

        String query = "SELECT * FROM " + DbSchema.TrackingTable.NAME +
                " WHERE " + DbSchema.TrackingTable.Cols.ACTIVE + " = 1 " +
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

    //******************** UTILS *******************

    private List commonGetListFromCursor(String query, String[] args, Class<? extends CustomWrapper> cElement) {

        List retList = new ArrayList();

        Cursor cursor = mDatabase.rawQuery(query, args);
        CustomWrapper cursorWrapper = null;
        try {
            cursorWrapper = cElement.getConstructor(Cursor.class).newInstance(cursor);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            if (cursorWrapper != null && cursorWrapper.moveToFirst()) {
                while (!cursorWrapper.isAfterLast()) {
                    retList.add(cursorWrapper.getElement());
                    cursorWrapper.moveToNext();
                }
            }
        } finally {
            if (cursorWrapper != null) {
                cursorWrapper.close();
            }
        }
        return retList;
    }

}
