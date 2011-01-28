package eu.sweetlygeek.sleepytime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

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
	private static final String GA_CODE = "UA-3472709-8";
	private static final GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();

	private SleepUtils() {
	}

	/**
	 * Track a page access
	 * 
	 * @param context
	 *            context
	 */
	public static void trackPage(Context context) {
		tracker.start(GA_CODE, context);
		tracker.trackPageView(context.getClass().getName());
		tracker.stop();
	}

	/**
	 * Track a button click
	 * 
	 * @param context
	 *            context
	 * @param buttonName
	 *            name of the button
	 */
	public static void trackButton(Context context, String buttonName) {
		tracker.start(GA_CODE, context);
		tracker.trackEvent("Clicks", "Button", buttonName, 1);
		tracker.stop();
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
