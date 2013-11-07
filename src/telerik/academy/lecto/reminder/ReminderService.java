package telerik.academy.lecto.reminder;

import telerik.academy.lecto.database.ReminderTable;
import android.content.Intent;

public class ReminderService extends WakeReminderIntentService {
	public ReminderService() {
		super("ReminderService");
	}

	@Override
	void doReminderWork(Intent intent) {
		Long rowId = intent.getExtras().getLong(ReminderTable.KEY_ROWID);
		// Status bar notification Code Goes here.
	}
}
