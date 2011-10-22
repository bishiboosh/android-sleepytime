package eu.sweetlygeek.sleepytime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Results activity
 * 
 * @author bishiboosh
 * 
 */
public class ResultsActivity extends Activity {

	private static final String ALARM_PLUS_MARKET_URI = "market://details?id=com.vp.alarmClockPlusDock";

	private static final String EXTRA_MINUTES = "android.intent.extra.alarm.MINUTES";

	private static final String EXTRA_HOUR = "android.intent.extra.alarm.HOUR";

	private static final String INTENT_SET_ALARM = "android.intent.action.SET_ALARM";

	private static final int MENU_ALARM = 0;

	private static final int DIALOG_SET_ALARM = 0;

	private static final int DIALOG_NO_ALARM = 1;

	private Choice choice;

	private Set<Date> times;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		Bundle extras = getIntent().getExtras();
		choice = (Choice) extras.getSerializable(SleepUtils.CHOICE_PARAM);
		TextView text1 = (TextView) findViewById(R.id.text1_results);
		TextView text2 = (TextView) findViewById(R.id.text2_results);
		TextView text3 = (TextView) findViewById(R.id.text3_results);
		SimpleDateFormat formatter = getFormatter();
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
				text3.setTextColor(Colors.LIGHT_PURPLE.getValue());
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
			timesText.setSpan(
					new ForegroundColorSpan(Colors.LIGHT_GREEN.getValue()),
					hoursPlacement.get(6), hoursPlacement.get(7),
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			for (int i = 8; i < 12; i += 2) {
				timesText.setSpan(
						new ForegroundColorSpan(Colors.DARK_GREEN.getValue()),
						hoursPlacement.get(i), hoursPlacement.get(i + 1),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		} else {
			for (int i = 0; i < hoursPlacement.size(); i += 2) {
				timesText.setSpan(
						new ForegroundColorSpan(Colors.GREEN.getValue()),
						hoursPlacement.get(i), hoursPlacement.get(i + 1),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		text2.setText(timesText);
	}

	private SimpleDateFormat getFormatter() {
		Locale locale = Locale.getDefault();
		String pattern;
		if (Locale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			pattern = "HH:mm";
		} else {
			pattern = "hh:mm aa";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (choice != Choice.CHOOSE_WAKE) {
			menu.add(Menu.NONE, MENU_ALARM, MENU_ALARM, R.string.add_alarm);
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == MENU_ALARM) {
			PackageManager pManager = getPackageManager();
			List<ResolveInfo> infos = pManager.queryIntentActivities(
					new Intent(INTENT_SET_ALARM),
					PackageManager.MATCH_DEFAULT_ONLY);
			showDialog(infos.isEmpty() ? DIALOG_NO_ALARM : DIALOG_SET_ALARM);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_SET_ALARM) {
			final List<Date> times = new ArrayList<Date>(this.times);
			List<String> dates = new ArrayList<String>();
			SimpleDateFormat formatter = getFormatter();
			for (Date d : times) {
				dates.add(formatter.format(d));
			}
			String[] items = dates.toArray(new String[dates.size()]);
			return new AlertDialog.Builder(this)
					.setTitle(R.string.select_hour_title)
					.setSingleChoiceItems(items, -1,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(INTENT_SET_ALARM);
									Date selected = times.get(which);
									Calendar c = Calendar.getInstance();
									c.setTime(selected);
									i.putExtra(EXTRA_HOUR,
											c.get(Calendar.HOUR_OF_DAY));
									i.putExtra(EXTRA_MINUTES,
											c.get(Calendar.MINUTE));
									ResultsActivity.this.startActivity(i);
									dialog.dismiss();
								}
							}).create();
		} else if (id == DIALOG_NO_ALARM)
			return new AlertDialog.Builder(this)
					.setTitle(R.string.no_alarm_title)
					.setMessage(R.string.no_alarm_message)
					.setPositiveButton(R.string.download_alarm_app,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(Intent.ACTION_VIEW);
									Uri uri = Uri.parse(ALARM_PLUS_MARKET_URI);
									i.setData(uri);
									ResultsActivity.this.startActivity(i);
								}
							}).create();
		else
			return super.onCreateDialog(id);
	}
}
