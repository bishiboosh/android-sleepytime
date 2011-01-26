package eu.sweetlygeek.sleepytime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Utils for calculating sleep time
 * 
 * @author bishiboosh
 * 
 */
public final class SleepUtils {

	public static final String DATE_PARAM = "date";
	public static final String NOW_PARAM = "now";

	private SleepUtils() {
	}

	/**
	 * Get times to wake. We add 14 minutes to fall asleep, and calculate the
	 * sleep cycles.
	 * 
	 * @return list of times when to get up
	 */
	public static List<Date> getWakingTimes() {
		Calendar fallAsleep = Calendar.getInstance();
		fallAsleep.add(Calendar.MINUTE, 14);
		List<Date> result = new ArrayList<Date>();
		for (int i = 1; i <= 6; i++) {
			fallAsleep.add(Calendar.MINUTE, 90);
			result.add(fallAsleep.getTime());
		}
		return result;
	}

	/**
	 * Get sleeping times corresponding to a local time
	 * 
	 * @param wakingTime
	 *            time to wake up !
	 * @return list of times one should go to bed to
	 */
	public static List<Date> getSleepingTimes(Date wakingTime) {
		List<Date> result = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(wakingTime);
		calendar.add(Calendar.MINUTE, -14);
		calendar.add(Calendar.MINUTE, -(2 * 90));
		for (int i = 3; i <= 6; i++) {
			calendar.add(Calendar.MINUTE, -90);
			result.add(calendar.getTime());
		}
		return result;
	}

	/**
	 * Get an adapter with a list of "00" items.
	 * 
	 * @param context
	 *            context
	 * @param min
	 *            minimum (inclusive)
	 * @param max
	 *            maximum (inclusive)
	 * @return array adapter
	 */
	public static ArrayAdapter<String> getIntAdapter(Context context, int min, int max) {
		DecimalFormat formatter = new DecimalFormat("00");
		List<String> list = new ArrayList<String>();
		for (int i = min; i <= max; i++) {
			list.add(formatter.format(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
}
