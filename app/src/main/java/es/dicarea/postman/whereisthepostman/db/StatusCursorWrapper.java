package es.dicarea.postman.whereisthepostman.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.dicarea.postman.whereisthepostman.StatusEnum;
import es.dicarea.postman.whereisthepostman.StatusItem;

public class StatusCursorWrapper extends CursorWrapper {
    public StatusCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public StatusItem getStatus() {
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
