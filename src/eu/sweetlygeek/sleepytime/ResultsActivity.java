package eu.sweetlygeek.sleepytime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Results activity
 * 
 * @author bishiboosh
 * 
 */
public class ResultsActivity extends Activity {

	private static final int DARK_GREEN = Color.parseColor("#00CC33");
	private static final int LIGHT_GREEN = Color.parseColor("#99CC66");
	private static final int GREEN = Color.parseColor("#01DF74");
	private static final int LIGHT_PURPLE = Color.parseColor("#9966CC");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		Bundle extras = getIntent().getExtras();
		boolean goToBedNow = extras.getBoolean(SleepUtils.NOW_PARAM);
		TextView text1 = (TextView) findViewById(R.id.text1_results);
		TextView text2 = (TextView) findViewById(R.id.text2_results);
		TextView text3 = (TextView) findViewById(R.id.text3_results);
		List<Date> times;
		if (goToBedNow) {
			text1.setText(R.string.results_wakeup);
			times = SleepUtils.getWakingTimes();
			text3.setText(R.string.night_sleep);
		} else {
			text1.setText(R.string.results_asleep);
			Date date = (Date) extras.getSerializable(SleepUtils.DATE_PARAM);
			times = SleepUtils.getSleepingTimes(date);
			text3.setText(R.string.sleep_cycles);
			text3.setTextColor(LIGHT_PURPLE);
		}
		Locale locale = Locale.getDefault();
		String pattern;
		if (Locale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			pattern = "HH:mm";
		} else {
			pattern = "hh:mm aa";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String or = getResources().getString(R.string.or);
		List<Integer> hoursPlacement = new ArrayList<Integer>();
		StringBuilder tBuilder = new StringBuilder();
		for (int i = 0; i < times.size(); i++) {
			hoursPlacement.add(tBuilder.length());
			tBuilder.append(formatter.format(times.get(i)));
			hoursPlacement.add(tBuilder.length());
			if (i != times.size() - 1) {
				tBuilder.append(" ");
				tBuilder.append(or);
				tBuilder.append(" ");
			}
		}
		SpannableString timesText = new SpannableString(tBuilder);
		if (goToBedNow) {
			timesText.setSpan(new ForegroundColorSpan(LIGHT_GREEN), hoursPlacement.get(6), hoursPlacement.get(7),
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			for (int i = 8; i < 12; i += 2) {
				timesText.setSpan(new ForegroundColorSpan(DARK_GREEN), hoursPlacement.get(i), hoursPlacement.get(i + 1),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		} else {
			for (int i = 0; i < hoursPlacement.size(); i += 2) {
				timesText.setSpan(new ForegroundColorSpan(GREEN), hoursPlacement.get(i), hoursPlacement.get(i + 1),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		text2.setText(timesText);
	}
}
