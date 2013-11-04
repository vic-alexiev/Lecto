package telerik.academy.lecto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ReminderEditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_edit);
		
		Intent intent = getIntent();
		if(intent != null) { 
			Bundle extras = intent.getExtras();
			int rowId = extras != null ? extras.getInt("RowId") : -1;
			// Do stuff with the row id here
			}
	}
}
