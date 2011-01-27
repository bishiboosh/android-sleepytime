package eu.sweetlygeek.sleepytime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utils for calculating sleep time
 * 
 * @author bishiboosh
 * 
 */
public final class SleepUtils {

	/** Date param for extra */
	public static final String DATE_PARAM = "date";
	/** Now param for extra */
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

}
