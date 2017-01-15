package es.dicarea.postman.whereisthepostman.db;

import android.database.Cursor;
import android.database.CursorWrapper;

public class LogCursorWrapper extends CursorWrapper {
    public LogCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Log getLog() {
        Integer id = getInt(getColumnIndex(DbSchema.LogTable.Cols.ID));
        Long date = getLong(getColumnIndex(DbSchema.LogTable.Cols.DATE));
        Integer status = getInt(getColumnIndex(DbSchema.LogTable.Cols.STATUS));

        Log log = new Log();
        log.setId(id);
        log.setDate(date);
        log.setStatus(status);

        return log;
    }
}
