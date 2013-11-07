package telerik.academy.lecto.reminder;

import telerik.academy.lecto.database.ReminderTable;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// When the AlarmManager broadcasts the pending intent, 
		// the OnAlarmReceiver class responds to the intent
		long rowid = intent.getExtras().getLong(ReminderTable.KEY_ROWID);
		WakeReminderIntentService.acquireStaticLock(context);
		Intent newIntent = new Intent(context, ReminderService.class);
		newIntent.putExtra(ReminderTable.KEY_ROWID, rowid);
		context.startService(newIntent);
	}
}
