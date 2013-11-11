package telerik.academy.lecto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import telerik.academy.lecto.contentprovider.ReminderContentProvider;
import telerik.academy.lecto.database.ReminderTable;
import telerik.academy.lecto.reminder.ReminderManager;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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

	private EditText mTitleText;
	private EditText mLocationText;
	private Button mConfirmButton;
	private Calendar mCalendar;
	private Button mDateButton;
	private Button mTimeButton;
	private Uri mReminderUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.reminder_edit);

		mCalendar = Calendar.getInstance();
		mTitleText = (EditText) findViewById(R.id.title);
		mLocationText = (EditText) findViewById(R.id.location);
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mTimeButton = (Button) findViewById(R.id.reminder_time);

		mConfirmButton = (Button) findViewById(R.id.confirm);

		Bundle extras = getIntent().getExtras();

		// check from the saved Instance
		mReminderUri = (savedInstanceState == null) ? null
				: (Uri) savedInstanceState
						.getParcelable(ReminderContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			mReminderUri = extras
					.getParcelable(ReminderContentProvider.CONTENT_ITEM_TYPE);
		}

		populateFields(mReminderUri);

		registerButtonListenersAndSetDefaultText();
	}

	@SuppressLint("SimpleDateFormat")
	private void populateFields(Uri uri) {

		if (uri != null) {
			// edit an existing reminder
			String[] projection = { ReminderTable.KEY_TITLE,
					ReminderTable.KEY_LOCATION, ReminderTable.KEY_DATE_TIME };
			Cursor cursor = getContentResolver().query(uri, projection, null,
					null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				mTitleText.setText(cursor.getString(cursor
						.getColumnIndexOrThrow(ReminderTable.KEY_TITLE)));

				mLocationText.setText(cursor.getString(cursor
						.getColumnIndexOrThrow(ReminderTable.KEY_LOCATION)));

				SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
						DATE_TIME_FORMAT);
				Date date = null;
				try {
					String dateString = cursor
							.getString(cursor
									.getColumnIndexOrThrow(ReminderTable.KEY_DATE_TIME));
					date = dateTimeFormat.parse(dateString);
					mCalendar.setTime(date);
				} catch (ParseException e) {
					Log.e("ReminderEditActivity", e.getMessage(), e);
				}

				// always close the cursor
				cursor.close();
			}
		} else {
			// create a new reminder
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String defaultTitleKey = getString(R.string.pref_lecture_title_key);
			String defaultTimeKey = getString(R.string.pref_default_time_from_now_key);
			String defaultTitle = prefs.getString(defaultTitleKey, "");
			String defaultTime = prefs.getString(defaultTimeKey, "");
			if ("".equals(defaultTitle) == false)
				mTitleText.setText(defaultTitle);
			if ("".equals(defaultTime) == false)
				mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));
		}

		updateDateButtonText();
		updateTimeButtonText();
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
				if (TextUtils.isEmpty(mTitleText.getText().toString())
						|| TextUtils
								.isEmpty(mLocationText.getText().toString())) {
					makeErrorToast();
				} else {
					setResult(RESULT_OK);
					makeSuccessToast();
					finish();
				}
			}
		});

		updateDateButtonText();
		updateTimeButtonText();
	}

	private void showDatePickerDialog() {
		DialogFragment dialogFragment = new DatePickerFragment();
		dialogFragment.show(getSupportFragmentManager(), "datePicker");
	}

	private void showTimePickerDialog() {
		DialogFragment dialogFragment = new TimePickerFragment();
		dialogFragment.show(getSupportFragmentManager(), "timePicker");
	}

	private void makeErrorToast() {
		Toast.makeText(ReminderEditActivity.this,
				getString(R.string.reminder_error_message), Toast.LENGTH_LONG)
				.show();
	}

	private void makeSuccessToast() {
		Toast.makeText(ReminderEditActivity.this,
				getString(R.string.reminder_saved_message), Toast.LENGTH_LONG)
				.show();
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		/*
		 * The onSaveInstanceState() method is called so that you may retrieve
		 * and store activity-level instance states in a Bundle. This method is
		 * called before the activity is killed so that when the activity comes
		 * back in the future, it can be restored to a known state (as done in
		 * the onResume() method).
		 */
		outState.putParcelable(ReminderContentProvider.CONTENT_ITEM_TYPE,
				mReminderUri);
	}

	@SuppressLint("SimpleDateFormat")
	private void saveState() {
		String title = mTitleText.getText().toString();
		String location = mLocationText.getText().toString();

		if (title.length() == 0 || location.length() == 0) {
			return;
		}

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

		ContentValues values = new ContentValues();
		values.put(ReminderTable.KEY_TITLE, title);
		values.put(ReminderTable.KEY_LOCATION, location);
		values.put(ReminderTable.KEY_DATE_TIME, reminderDateTime);

		if (mReminderUri == null) {
			// New reminder
			mReminderUri = getContentResolver().insert(
					ReminderContentProvider.CONTENT_URI, values);
		} else {
			// Update reminder
			getContentResolver().update(mReminderUri, values, null, null);
		}

		String rowId = mReminderUri.getLastPathSegment();
		new ReminderManager(this).setReminder(Long.parseLong(rowId),
				(Calendar) mCalendar);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
}
