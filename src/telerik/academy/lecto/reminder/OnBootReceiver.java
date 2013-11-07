package telerik.academy.lecto.reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import telerik.academy.lecto.ReminderEditActivity;
import telerik.academy.lecto.contentprovider.ReminderContentProvider;
import telerik.academy.lecto.database.ReminderTable;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context context, Intent intent) {

		ReminderManager reminderManager = new ReminderManager(context);

		String[] projection = { ReminderTable.KEY_TITLE,
				ReminderTable.KEY_LOCATION, ReminderTable.KEY_DATE_TIME };
		Cursor cursor = context.getContentResolver().query(
				ReminderContentProvider.CONTENT_URI, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			int rowIdColumnIndex = cursor
					.getColumnIndex(ReminderTable.KEY_ROWID);
			int dateTimeColumnIndex = cursor
					.getColumnIndex(ReminderTable.KEY_DATE_TIME);
			while (cursor.isAfterLast() == false) {
				Log.d("OnBootReceiver", "Adding alarm from boot.");
				Log.d("OnBootReceiver", "Row Id Column Index - "
						+ rowIdColumnIndex);

				Long rowId = cursor.getLong(rowIdColumnIndex);
				String dateTime = cursor.getString(dateTimeColumnIndex);
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(
						ReminderEditActivity.DATE_TIME_FORMAT);
				try {
					Date date = format.parse(dateTime);
					calendar.setTime(date);
					reminderManager.setReminder(rowId, calendar);
				} catch (ParseException e) {
					Log.e("OnBootReceiver", e.getMessage(), e);
				}

				cursor.moveToNext();
			}

			cursor.close();
		}
	}
}
