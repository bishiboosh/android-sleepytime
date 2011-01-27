package eu.sweetlygeek.sleepytime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Results activity
 * 
 * @author bishiboosh
 * 
 */
public class ResultsActivity extends Activity {

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
		}
		Locale locale = Locale.getDefault();
		String pattern;
		if (locale == Locale.FRENCH) {
			pattern = "HH:mm";
		} else {
			pattern = "hh:mm";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String or = getResources().getString(R.string.or);
		StringBuilder tBuilder = new StringBuilder();
		for (int i = 0; i < times.size(); i++) {
			tBuilder.append(formatter.format(times.get(i)));
			if (i != times.size() - 1) {
				tBuilder.append(" ");
				tBuilder.append(or);
				tBuilder.append(" ");
				if (i > 0 && i % 3 == 2) {
					tBuilder.append("\n");
				}
			}
		}
		text2.setText(tBuilder);
	}

}
