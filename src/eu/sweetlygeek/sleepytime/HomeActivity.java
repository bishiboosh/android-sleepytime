package eu.sweetlygeek.sleepytime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class HomeActivity extends Activity {

	private static final String MINUTE_PARAM = "minute";
	private static final String HOUR_PARAM = "hour";
	private static final DecimalFormat FORMATTER = new DecimalFormat("00");
	private static final Locale LOCALE = Locale.getDefault();
	private static final List<String> HOURS;
	private static final List<String> MINUTES;
	static {
		int maxHours = LOCALE == Locale.FRENCH ? 24 : 12;
		HOURS = new ArrayList<String>();
		for (int i = 1; i <= maxHours; i++) {
			HOURS.add(FORMATTER.format(i));
		}
		MINUTES = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			MINUTES.add(FORMATTER.format(i));
		}
	}

	private boolean hourChosen;
	private boolean minuteChosen;
	private boolean is24h;

	private Spinner hours;
	private Spinner minutes;
	private Spinner ampm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		hourChosen = false;
		minuteChosen = false;
		hours = (Spinner) findViewById(R.id.hour);
		ArrayAdapter<String> hAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, HOURS);
		hAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		hours.setAdapter(hAdapter);
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
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MINUTES);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		minutes.setAdapter(mAdapter);
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
		if (LOCALE == Locale.FRENCH) {
			ampm.setVisibility(View.GONE);
			is24h = true;
		}
		Button zzz = (Button) findViewById(R.id.zzz);
		zzz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LocalTime now = new LocalTime();
				int hour = now.hourOfDay().get();
				int minute = now.minuteOfHour().get();
				goToNext(hour, minute);
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
		goToNext(hour, minute);
	}

	private void goToNext(int hour, int minute) {
		Intent i = new Intent();
		i.putExtra(HOUR_PARAM, hour);
		i.putExtra(MINUTE_PARAM, minute);
	}
}