package telerik.academy.lecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RemindersDbAdapter {

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "reminders";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_TITLE = "title";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DATE_TIME = "reminder_date_time";
	public static final String KEY_ROWID = "_id";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_LOCATION + " text not null, "
			+ KEY_DATE_TIME + " text not null);";

	private final Context mContext;

	public RemindersDbAdapter(Context context) {
		this.mContext = context;
	}

	public RemindersDbAdapter open() throws android.database.SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createReminder(String title, String location,
			String reminderDateTime) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_LOCATION, location);
		initialValues.put(KEY_DATE_TIME, reminderDateTime);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteReminder(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllReminders() {
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_LOCATION, KEY_DATE_TIME }, null, null, null, null, null);
	}

	public Cursor fetchReminder(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_TITLE, KEY_LOCATION, KEY_DATE_TIME }, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean updateReminder(long rowId, String title, String location,
			String reminderDateTime) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_LOCATION, location);
		args.put(KEY_DATE_TIME, reminderDateTime);
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Not used, but you could upgrade the database with ALTER
			// Scripts
		}
	}
}