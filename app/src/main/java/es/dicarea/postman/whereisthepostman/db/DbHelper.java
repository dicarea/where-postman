package es.dicarea.postman.whereisthepostman.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "gesPagos.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + DbSchema.LogTable.NAME + "(" +
                DbSchema.LogTable.Cols.ID + " integer primary key autoincrement, " +
                DbSchema.LogTable.Cols.DATE + ", " +
                DbSchema.LogTable.Cols.STATUS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

}
