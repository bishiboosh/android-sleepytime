package eu.sweetlygeek.sleepytime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Default activity
 * 
 * @author bishiboosh
 * 
 */
public class HomeActivity extends Activity {

	private boolean hourChosen;
	private boolean minuteChosen;
	private boolean is24h;

	private Spinner hours;
	private Spinner minutes;
	private Spinner ampm;

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
		Locale locale = Locale.getDefault();
		hourChosen = false;
		minuteChosen = false;
		hours = (Spinner) findViewById(R.id.hour);
		int min, max;
		if (locale == Locale.FRENCH) {
			is24h = true;
			min = 0;
			max = 23;
		} else {
			min = 1;
			max = 12;
		}
		hours.setAdapter(SleepUtils.getIntAdapter(this, min, max));
		hours.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				hourChosen = true;
				if (minuteChosen) {
					goToNext();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		minutes = (Spinner) findViewById(R.id.minute);
		minutes.setAdapter(SleepUtils.getIntAdapter(this, 0, 59));
		minutes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				minuteChosen = true;
				if (hourChosen) {
					goToNext();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		ampm = (Spinner) findViewById(R.id.ampm);
		ampm.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (minuteChosen && hourChosen) {
					goToNext();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		if (is24h)
			ampm.setVisibility(View.GONE);
		Button zzz = (Button) findViewById(R.id.zzz);
		zzz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToNext(null, true);
			}
		});
	}

	private void goToNext() {
		int hour = Integer.parseInt((String) hours.getSelectedItem());
		int minute = Integer.parseInt((String) hours.getSelectedItem());
		if (!is24h) {
			String sAmPm = (String) ampm.getSelectedItem();
			MidDay ampm = MidDay.AM;
			if (sAmPm != null)
				ampm = MidDay.valueOf(sAmPm);
			if (ampm == MidDay.PM)
				hour += 12;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		goToNext(calendar.getTime(), false);
	}

	private void goToNext(Date date, boolean sleepNow) {
		Intent i = new Intent(this, ResultsActivity.class);
		i.putExtra(SleepUtils.DATE_PARAM, date);
		i.putExtra(SleepUtils.NOW_PARAM, sleepNow);
		startActivity(i);
	}
}