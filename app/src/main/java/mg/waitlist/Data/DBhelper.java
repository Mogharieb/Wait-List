package mg.waitlist.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mg.waitlist.Data.Contract.waitlistEntry;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "waitlist.db";
    private static final int DATABASE_VERSION = 1;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + waitlistEntry.TABLE_NAME + " (" +
                waitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                waitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                waitlistEntry.COLUMN_PARTY_SIZE + " TEXT NOT NULL, " +
                waitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS  " + waitlistEntry.TABLE_NAME);
        onCreate(db);
    }
}
