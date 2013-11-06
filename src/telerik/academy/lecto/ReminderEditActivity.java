package telerik.academy.lecto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderEditActivity extends FragmentActivity implements
		OnDateSetListener, OnTimeSetListener {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "kk:mm";

	private RemindersDbAdapter mDbHelper;
	private EditText mTitleText;
	private EditText mLocationText;
	private Button mConfirmButton;
	private Calendar mCalendar;
	private Button mDateButton;
	private Button mTimeButton;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new RemindersDbAdapter(this);

		setContentView(R.layout.reminder_edit);

		mCalendar = Calendar.getInstance();
		mTitleText = (EditText) findViewById(R.id.title);
		mLocationText = (EditText) findViewById(R.id.location);
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mTimeButton = (Button) findViewById(R.id.reminder_time);

		mConfirmButton = (Button) findViewById(R.id.confirm);

		mRowId = savedInstanceState != null ? savedInstanceState
				.getLong(RemindersDbAdapter.KEY_ROWID) : null;

		registerButtonListenersAndSetDefaultText();
	}

	private void registerButtonListenersAndSetDefaultText() {
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDatePickerDialog();
			}
		});

		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTimePickerDialog();
			}
		});

		mConfirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				saveState();

				// This result code (member of the Activity class)
				// can be inspected on ReminderListActivity
				// in the onActivityResult() method.
				setResult(RESULT_OK);
				Toast.makeText(ReminderEditActivity.this,
						getString(R.string.lecture_saved_message),
						Toast.LENGTH_SHORT).show();

				// close ReminderEditActivity and return the specified
				// result code (RESULT_OK) to the caller -
				// ReminderListActivity's createReminder() method
				finish();
			}
		});

		updateDateButtonText();
		updateTimeButtonText();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Before the activity is shut down or 
		// when it’s paused, the database is closed.
		mDbHelper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open();
		setRowIdFromIntent();
		populateFields();
	}

	private void setRowIdFromIntent() {
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras
					.getLong(RemindersDbAdapter.KEY_ROWID) : null;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	private void populateFields() {
		if (mRowId != null) {
			Cursor reminder = mDbHelper.fetchReminder(mRowId);
			startManagingCursor(reminder);
			mTitleText.setText(reminder.getString(reminder
					.getColumnIndexOrThrow(RemindersDbAdapter.KEY_TITLE)));
			mLocationText.setText(reminder.getString(reminder
					.getColumnIndexOrThrow(RemindersDbAdapter.KEY_LOCATION)));
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
					DATE_TIME_FORMAT);
			Date date = null;
			try {
				String dateString = reminder
						.getString(reminder
								.getColumnIndexOrThrow(RemindersDbAdapter.KEY_DATE_TIME));
				date = dateTimeFormat.parse(dateString);
				mCalendar.setTime(date);
			} catch (ParseException e) {
				Log.e("ReminderEditActivity", e.getMessage(), e);
			}
		}

		updateDateButtonText();
		updateTimeButtonText();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		/* The onSaveInstanceState() method is called so that 
		 * you may retrieve and store activity-level instance 
		 * states in a Bundle. This method is called before 
		 * the activity is killed so that when the activity 
		 * comes back in the future, it can be restored to 
		 * a known state (as done in the onResume() method).*/
		outState.putLong(RemindersDbAdapter.KEY_ROWID, mRowId);
	}

	@SuppressLint("SimpleDateFormat")
	private void updateDateButtonText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String dateForButton = dateFormat.format(mCalendar.getTime());
		mDateButton.setText(dateForButton);
	}

	@SuppressLint("SimpleDateFormat")
	private void updateTimeButtonText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
		String timeForButton = dateFormat.format(mCalendar.getTime());
		mTimeButton.setText(timeForButton);
	}

	public void showDatePickerDialog() {
		DialogFragment dialogFragment = new DatePickerFragment();
		dialogFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog() {
		DialogFragment dialogFragment = new TimePickerFragment();
		dialogFragment.show(getSupportFragmentManager(), "timePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear);
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		updateDateButtonText();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);

		updateTimeButtonText();
	}

	@SuppressLint("SimpleDateFormat")
	private void saveState() {
		String title = mTitleText.getText().toString();
		String location = mLocationText.getText().toString();

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

		if (mRowId == null) {
			long id = mDbHelper.createReminder(title, location,
					reminderDateTime);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateReminder(mRowId, title, location, reminderDateTime);
		}
	}
}
