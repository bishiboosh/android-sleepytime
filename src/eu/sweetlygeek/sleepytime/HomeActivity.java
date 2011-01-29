package eu.sweetlygeek.sleepytime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
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
	private final static int ABOUT_DIALOG = 1;

	private Choice choice;

	/** Called when the activity is first created. */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SleepUtils.trackPage(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button chooseTime = (Button) findViewById(R.id.choose_time);
		chooseTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SleepUtils.trackButton(HomeActivity.this, "Choose time");
				choice = Choice.CHOOSE_WAKE;
				showDialog(TIME_PICKER_DIALOG);
			}
		});
		Button chooseBedTime = (Button) findViewById(R.id.choose_bed_time);
		chooseBedTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SleepUtils.trackButton(HomeActivity.this, "Choose bed time");
				choice = Choice.CHOOSE_BED;
				showDialog(TIME_PICKER_DIALOG);
			}
		});
		Button zzz = (Button) findViewById(R.id.zzz);
		zzz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SleepUtils.trackButton(HomeActivity.this, "Go to bed now");
				goToNext(null, Choice.BED_NOW);
			}
		});
		Button about = (Button) findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SleepUtils.trackButton(HomeActivity.this, "About");
				showDialog(ABOUT_DIALOG);
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
						goToNext(calendar.getTime(), choice);
					}
				}, 0, 0, is24h);
			case ABOUT_DIALOG:
				return new AlertDialog.Builder(this).setTitle(R.string.about).setMessage(R.string.about_text).create();
		}
		return null;
	}

	private void goToNext(Date date, Choice choice) {
		Intent i = new Intent(this, ResultsActivity.class);
		i.putExtra(SleepUtils.DATE_PARAM, date);
		i.putExtra(SleepUtils.CHOICE_PARAM, choice);
		startActivity(i);
	}
}