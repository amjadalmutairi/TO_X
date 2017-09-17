package me.amjadalmutairi.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amjadalmutairi on 9/17/17.
 */

public class ItemHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "item.db";
    public static final int DATABASE_VERSION = 1;

    public ItemHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " (" +
                ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                ItemContract.ItemEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                ItemContract.ItemEntry.COLUMN_CATEGORY + " TEXT NOT NULL," +
                ItemContract.ItemEntry.COLUMN_COMPLETED + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
