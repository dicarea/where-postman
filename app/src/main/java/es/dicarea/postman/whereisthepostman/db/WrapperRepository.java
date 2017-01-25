package es.dicarea.postman.whereisthepostman.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.StatusCorreosEnum;

public class WrapperRepository {

    public static class StatusCursorWrapper extends CursorWrapper {

        public StatusCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public StatusItem getElement() {
            Integer id = getInt(getColumnIndex(DbSchema.StatusTable.Cols.ID));
            Long time = getLong(getColumnIndex(DbSchema.StatusTable.Cols.DATE));
            Integer status = getInt(getColumnIndex(DbSchema.StatusTable.Cols.STATUS));
            Integer trackingId = getInt(getColumnIndex(DbSchema.StatusTable.Cols.TRACKING_ID));

            StatusItem statusItem = new StatusItem();
            statusItem.setId(id);
            statusItem.setTime(time);
            statusItem.setStatus(StatusCorreosEnum.getStatus(status));

            TrackingItem tracking = new TrackingItem();
            tracking.setId(trackingId);
            statusItem.setTracking(tracking);

            return statusItem;
        }

    }

    public static class TrackingCursorWrapper extends CursorWrapper {

        public TrackingCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public TrackingItem getElement() {
            Integer id = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.ID));
            String code = getString(getColumnIndex(DbSchema.TrackingTable.Cols.CODE));
            String desc = getString(getColumnIndex(DbSchema.TrackingTable.Cols.DESC));
            Integer activeI = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.ACTIVE));
            Integer deletedI = getInt(getColumnIndex(DbSchema.TrackingTable.Cols.DELETED));

            TrackingItem trackingItem = new TrackingItem();
            trackingItem.setId(id);
            trackingItem.setCode(code);
            trackingItem.setDesc(desc);
            trackingItem.setActive(activeI != null && activeI != 0);
            trackingItem.setDeleted(deletedI != null && deletedI != 0);

            return trackingItem;
        }

    }

}