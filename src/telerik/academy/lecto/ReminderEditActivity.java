package telerik.academy.lecto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class ReminderEditActivity extends FragmentActivity implements
		OnDateSetListener, OnTimeSetListener {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "kk:mm";

	private Calendar mCalendar;
	private Button mDateButton;
	private Button mTimeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_edit);

		mCalendar = Calendar.getInstance();
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mTimeButton = (Button) findViewById(R.id.reminder_time);

		registerButtonListenersAndSetDefaultText();

		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			int rowId = extras != null ? extras.getInt("RowId") : -1;
			// Do stuff with the row id here
		}
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

		updateDateButtonText();
		updateTimeButtonText();
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
}
