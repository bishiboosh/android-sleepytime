package eu.sweetlygeek.sleepytime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;

/**
 * Widget provider.
 * 
 * @author bishiboosh
 * 
 */
public class WidgetProvider extends AppWidgetProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.appwidget.AppWidgetProvider#onUpdate(android.content.Context,
	 * android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			Intent intent = new Intent(context, HomeActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

			RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			rViews.setOnClickPendingIntent(R.id.widget_layout, pIntent);

			String pattern;
			if (Locale.FRENCH.getLanguage().equals(Locale.getDefault().getLanguage())) {
				pattern = "HH:mm";
			} else {
				pattern = "hh:mm aa";
			}
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			rViews.setTextViewText(R.id.text1_widget, context.getString(R.string.results_widget));
			Set<Date> times = SleepUtils.getWakingTimes();
			String or = context.getString(R.string.or);
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
			timesText.setSpan(new ForegroundColorSpan(Colors.GREEN.getValue()), hoursPlacement.get(6), hoursPlacement.get(7),
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			for (int i = 8; i < 12; i += 2) {
				timesText.setSpan(new ForegroundColorSpan(Colors.DARK_GREEN.getValue()), hoursPlacement.get(i),
						hoursPlacement.get(i + 1), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
			rViews.setTextViewText(R.id.text2_widget, timesText);

			appWidgetManager.updateAppWidget(appWidgetId, rViews);
		}
	}

}
