
package eu.sweetlygeek.sleepytime.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import eu.sweetlygeek.sleepytime.R;
import eu.sweetlygeek.sleepytime.activity.fragment.AlarmDialogFragment;
import eu.sweetlygeek.sleepytime.model.Choice;
import eu.sweetlygeek.sleepytime.model.Colors;
import eu.sweetlygeek.sleepytime.utils.SleepUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Results activity
 * 
 * @author bishiboosh
 */
public class ResultsActivity extends SherlockFragmentActivity {

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
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        return formatter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Alarm clock only available for >= 9
        if (SleepUtils.getSdkVersion() >= Build.VERSION_CODES.GINGERBREAD) {
            menu.add(R.string.add_alarm).setIcon(R.drawable.perm_group_device_alarms)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment fragment = new AlarmDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(AlarmDialogFragment.TIMES_KEY, new ArrayList<Date>(this.times));
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "alarmDialog");
        return true;
    }
}
