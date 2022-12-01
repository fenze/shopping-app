package dev.fenze.school;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OrderDatabase extends SQLiteOpenHelper {
    static String[] cells;

    public OrderDatabase(Context c) {
        super(c, "orders.sqlite", null, 2);
        cells = new String[]{"ID", "USER", "PHONE", "DATE", "DATA"};
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + "ORDERS" + "("
            + cells[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + cells[1] + " TEXT,"
            + cells[2] + " TEXT,"
            + cells[3] + " INTEGER,"
            + cells[4] + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "ORDERS");
        onCreate(db);
    }

    void addData(String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(cells[3], data);

        db.insert("ORDERS", null, cv);
    }
}
