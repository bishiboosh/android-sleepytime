package eu.sweetlygeek.sleepytime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
		SleepUtils.trackPage(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		Bundle extras = getIntent().getExtras();
		Choice choice = (Choice) extras.getSerializable(SleepUtils.CHOICE_PARAM);
		TextView text1 = (TextView) findViewById(R.id.text1_results);
		TextView text2 = (TextView) findViewById(R.id.text2_results);
		TextView text3 = (TextView) findViewById(R.id.text3_results);
		Locale locale = Locale.getDefault();
		String pattern;
		if (Locale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			pattern = "HH:mm";
		} else {
			pattern = "hh:mm aa";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Set<Date> times;
		Date date = (Date) extras.getSerializable(SleepUtils.DATE_PARAM);
		switch (choice) {
			case BED_NOW:
				text1.setText(R.string.results_wakeup);
				times = SleepUtils.getWakingTimes();
				text3.setText(R.string.night_sleep);
				break;
			case CHOOSE_BED:
				text1.setText(R.string.results_wakeup_choose);
				times = SleepUtils.getWakingTime(date);
				text3.setText(R.string.night_sleep);
				break;
			case CHOOSE_WAKE:
				text1.setText(R.string.results_asleep);
				times = SleepUtils.getSleepingTimes(date);
				text3.setText(R.string.sleep_cycles);
				text3.setTextColor(LIGHT_PURPLE);
				break;
			default:
				times = null;
		}
		String or = getResources().getString(R.string.or);
		List<Integer> hoursPlacement = new ArrayList<Integer>();
		StringBuilder tBuilder = new StringBuilder();
		for (Iterator<Date> it = times.iterator(); it.hasNext();) {
			hoursPlacement.add(tBuilder.length());
			tBuilder.append(formatter.format(it.next()));
			hoursPlacement.add(tBuilder.length());
			if (it.hasNext()) {
				tBuilder.append(" ");
				tBuilder.append(or);
				tBuilder.append(" ");
			}
		}
		SpannableString timesText = new SpannableString(tBuilder);
		if (choice == Choice.BED_NOW || choice == Choice.CHOOSE_BED) {
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
