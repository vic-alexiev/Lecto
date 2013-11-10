package telerik.academy.lecto.reminder;

import telerik.academy.lecto.R;
import telerik.academy.lecto.ReminderEditActivity;
import telerik.academy.lecto.contentprovider.ReminderContentProvider;
import telerik.academy.lecto.database.ReminderTable;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

public class ReminderService extends WakeReminderIntentService {

	public ReminderService() {
		super("ReminderService");
	}

	@Override
	void doReminderWork(Intent intent) {
		Long rowId = intent.getExtras().getLong(ReminderTable.KEY_ROWID);

		// Status bar notification Code Goes here.
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, ReminderEditActivity.class);

		Uri reminderUri = Uri.parse(ReminderContentProvider.CONTENT_URI + "/"
				+ rowId);
		// place the ID of the task to be edited into the intent
		notificationIntent.putExtra(ReminderContentProvider.CONTENT_ITEM_TYPE,
				reminderUri);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		Notification notification = new Notification.Builder(this)
				.setContentIntent(pendingIntent)
				.setContentTitle(getString(R.string.notifiy_new_lecture_title))
				.setContentText(getString(R.string.notify_new_lecture_message))
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setWhen(System.currentTimeMillis()).build();

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// An issue could occur if user ever enters over 2,147,483,647 tasks.
		// (Max int value).
		// I highly doubt this will ever happen. But is good to note.
		int id = (int) ((long) rowId);
		notificationManager.notify(id, notification);
	}
}
