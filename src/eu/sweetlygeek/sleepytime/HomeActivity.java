package eu.sweetlygeek.sleepytime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Default activity
 * 
 * @author bishiboosh
 * 
 */
public class HomeActivity extends Activity {

	private final static int TIME_PICKER_DIALOG = 0;

	/** Called when the activity is first created. */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button chooseTime = (Button) findViewById(R.id.choose_time);
		chooseTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_PICKER_DIALOG);
			}
		});
		Button zzz = (Button) findViewById(R.id.zzz);
		zzz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToNext(null, true);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case TIME_PICKER_DIALOG:
				String language = Locale.getDefault().getLanguage();
				boolean is24h = Locale.FRENCH.getLanguage().equals(language);
				return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						goToNext(calendar.getTime(), false);
					}
				}, 0, 0, is24h);
		}
		return null;
	}

	private void goToNext(Date date, boolean sleepNow) {
		Intent i = new Intent(this, ResultsActivity.class);
		i.putExtra(SleepUtils.DATE_PARAM, date);
		i.putExtra(SleepUtils.NOW_PARAM, sleepNow);
		startActivity(i);
	}
}