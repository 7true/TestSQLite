package tk.alltrue.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import tk.alltrue.data.HotelContract.GuestEntry;

public class HotelDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HotelDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "hotel.db";

    private static final int DATABASE_VERSION = 1;

    public HotelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + GuestEntry.TABLE_NAME + " ("
                + GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GuestEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + GuestEntry.COLUMN_CITY + " TEXT NOT NULL, "
                + GuestEntry.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 3, "
                + GuestEntry.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GuestEntry.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }

}
