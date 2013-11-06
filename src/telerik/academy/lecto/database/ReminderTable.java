package telerik.academy.lecto.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReminderTable {

	public static final String DATABASE_TABLE = "reminders";
	public static final String KEY_TITLE = "title";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DATE_TIME = "reminder_date_time";
	public static final String KEY_ROWID = "_id";

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_LOCATION + " text not null, "
			+ KEY_DATE_TIME + " text not null);";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ReminderTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		onCreate(db);
	}
}
