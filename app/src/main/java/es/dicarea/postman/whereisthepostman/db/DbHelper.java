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

        db.execSQL("create table " + DbSchema.StatusTable.NAME + "(" +
                DbSchema.StatusTable.Cols.ID + " integer primary key autoincrement, " +
                DbSchema.StatusTable.Cols.DATE + ", " +
                DbSchema.StatusTable.Cols.STATUS + ", " +
                DbSchema.StatusTable.Cols.CODE +
                ")"
        );

        db.execSQL("create table " + DbSchema.TrackingTable.NAME + "(" +
                DbSchema.TrackingTable.Cols.ID + " integer primary key autoincrement, " +
                DbSchema.TrackingTable.Cols.CODE + ", " +
                DbSchema.TrackingTable.Cols.ACTIVE + ", " +
                DbSchema.TrackingTable.Cols.DELETED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

}
