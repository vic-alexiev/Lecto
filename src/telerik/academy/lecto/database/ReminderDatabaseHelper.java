package telerik.academy.lecto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 1;

	public ReminderDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase db) {
		ReminderTable.onCreate(db);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ReminderTable.onUpgrade(db, oldVersion, newVersion);
	}
}
