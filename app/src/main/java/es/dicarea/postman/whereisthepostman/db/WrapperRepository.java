package es.dicarea.postman.whereisthepostman.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.StatusEnum;

public class WrapperRepository {

    public abstract static class CustomWrapper<T> extends CursorWrapper {
        public CustomWrapper(Cursor cursor) {
            super(cursor);
        }

        public abstract T getElement();
    }

    public static class StatusCursorWrapper extends CustomWrapper {

        public StatusCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public StatusItem getElement() {
            Integer id = getInt(getColumnIndex(DbSchema.StatusTable.Cols.ID));
            Long time = getLong(getColumnIndex(DbSchema.StatusTable.Cols.DATE));
            Integer status = getInt(getColumnIndex(DbSchema.StatusTable.Cols.STATUS));
            String code = getString(getColumnIndex(DbSchema.StatusTable.Cols.CODE));

            StatusItem statusItem = new StatusItem();
            statusItem.setId(id);
            statusItem.setTime(time);
            statusItem.setStatus(StatusEnum.getStatus(status));
            statusItem.setCode(code);

            return statusItem;
        }

    }

    public static class TrackingCursorWrapper extends CustomWrapper {

        public TrackingCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public TrackingItem getElement() {
            Integer id = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.ID));
            String code = getString(getColumnIndex(DbSchema.TrackingTable.Cols.CODE));
            Integer activeI = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.ACTIVE));
            Integer deletedI = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.DELETED));

            TrackingItem trackingItem = new TrackingItem();
            trackingItem.setId(id);
            trackingItem.setCode(code);
            trackingItem.setActive(activeI != null && activeI != 0);
            trackingItem.setDeleted(deletedI != null && deletedI != 0);

            return trackingItem;
        }

    }

}