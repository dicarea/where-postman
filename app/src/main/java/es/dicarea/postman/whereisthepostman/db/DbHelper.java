package es.dicarea.postman.whereisthepostman.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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
                DbSchema.StatusTable.Cols.TRACKING_ID +
                ")"
        );

        db.execSQL("create table " + DbSchema.TrackingTable.NAME + "(" +
                DbSchema.TrackingTable.Cols.ID + " integer primary key autoincrement, " +
                DbSchema.TrackingTable.Cols.CODE + ", " +
                DbSchema.TrackingTable.Cols.ACTIVE + ", " +
                DbSchema.TrackingTable.Cols.DELETED +
                ")"
        );

        ContentValues values = new ContentValues();
        values.put(DbSchema.TrackingTable.Cols.CODE, "PQ4F6P0703142520133205G");
        values.put(DbSchema.TrackingTable.Cols.ACTIVE, true);
        values.put(DbSchema.TrackingTable.Cols.DELETED, false);

        db.insert(DbSchema.TrackingTable.NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }


    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }
}
