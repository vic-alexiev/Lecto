package telerik.academy.lecto.reminder;

import java.util.Calendar;

import telerik.academy.lecto.database.ReminderTable;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderManager {

	private Context mContext;
	private AlarmManager mAlarmManager;

	public ReminderManager(Context context) {
		mContext = context;
		mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void setReminder(Long taskId, Calendar when) {
		Intent intent = new Intent(mContext, OnAlarmReceiver.class);
		intent.putExtra(ReminderTable.KEY_ROWID, (long) taskId);
		// the pending intent to be broadcast by the AlarmManager
		// FLAG_ONE_SHOT indicates that this PendingIntent can only be used once
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// The AlarmManager operates in a separate process, and for the
		// AlarmManager to notify an application that an action needs to be
		// performed, a PendingIntent must be created.
		// The alarm will now go off at the time requested.
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),
				pendingIntent);
	}
}
