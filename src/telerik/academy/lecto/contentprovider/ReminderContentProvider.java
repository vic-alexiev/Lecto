package telerik.academy.lecto.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import telerik.academy.lecto.database.ReminderDatabaseHelper;
import telerik.academy.lecto.database.ReminderTable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ReminderContentProvider extends ContentProvider {

	// database
	private ReminderDatabaseHelper mDbHelper;

	// used for the UriMatcher
	private static final int REMINDERS = 10;
	private static final int REMINDER_ID = 20;

	private static final String AUTHORITY = "telerik.academy.lecto.contentprovider";

	private static final String BASE_PATH = "reminders";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/reminders";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/reminder";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, REMINDERS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", REMINDER_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new ReminderDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(ReminderTable.DATABASE_TABLE);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case REMINDERS:
			break;
		case REMINDER_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(ReminderTable.KEY_ROWID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		long id = 0;

		switch (uriType) {
		case REMINDERS:
			id = db.insert(ReminderTable.DATABASE_TABLE, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case REMINDERS:
			rowsDeleted = db.delete(ReminderTable.DATABASE_TABLE, selection,
					selectionArgs);
			break;
		case REMINDER_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(ReminderTable.DATABASE_TABLE,
						ReminderTable.KEY_ROWID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(ReminderTable.DATABASE_TABLE,
						ReminderTable.KEY_ROWID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case REMINDERS:
			rowsUpdated = db.update(ReminderTable.DATABASE_TABLE, values,
					selection, selectionArgs);
			break;
		case REMINDER_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(ReminderTable.DATABASE_TABLE, values,
						ReminderTable.KEY_ROWID + "=" + id, null);
			} else {
				rowsUpdated = db.update(ReminderTable.DATABASE_TABLE, values,
						ReminderTable.KEY_ROWID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { ReminderTable.KEY_TITLE,
				ReminderTable.KEY_LOCATION, ReminderTable.KEY_DATE_TIME,
				ReminderTable.KEY_ROWID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
