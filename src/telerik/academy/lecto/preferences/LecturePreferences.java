package telerik.academy.lecto.preferences;

import telerik.academy.lecto.R;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.method.DigitsKeyListener;

public class LecturePreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new LecturePreferencesFragment())
				.commit();
	}

	public static class LecturePreferencesFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.lecture_preferences);

			EditTextPreference timeDefault = (EditTextPreference) findPreference(getString(R.string.pref_default_time_from_now_key));

			timeDefault.getEditText().setKeyListener(
					DigitsKeyListener.getInstance());
		}
	}
}
