package me.amjadalmutairi.todo.data;

import android.provider.BaseColumns;

/**
 * Created by amjadalmutairi on 9/17/17.
 */

public class ItemContract   {

    private ItemContract() {
    }

    public class ItemEntry implements BaseColumns{

        public static final String TABLE_NAME = "Item";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE  = "title";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_COMPLETED = "completed";

        public static final int COMPLETED = 1;
        public static final int NOT_COMPLETED = 0;

    }
}
