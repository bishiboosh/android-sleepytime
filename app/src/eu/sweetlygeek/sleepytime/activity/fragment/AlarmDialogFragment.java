
package eu.sweetlygeek.sleepytime.activity.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;

import com.actionbarsherlock.app.SherlockDialogFragment;

import eu.sweetlygeek.sleepytime.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Alarm dialog
 * 
 * @author valentin.rocher
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class AlarmDialogFragment extends SherlockDialogFragment {

    public static final String TIMES_KEY = "times";

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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressWarnings("unchecked")
        final List<Date> times = (List<Date>) getArguments()
                .getSerializable(TIMES_KEY);
        List<String> dates = new ArrayList<String>();
        SimpleDateFormat formatter = getFormatter();
        for (Date d : times) {
            dates.add(formatter.format(d));
        }
        String[] items = dates.toArray(new String[dates.size()]);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_hour_title)
                .setSingleChoiceItems(items, -1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                                Date selected = times.get(which);
                                Calendar c = Calendar.getInstance();
                                c.setTime(selected);
                                i.putExtra(AlarmClock.EXTRA_HOUR,
                                        c.get(Calendar.HOUR_OF_DAY));
                                i.putExtra(AlarmClock.EXTRA_MINUTES,
                                        c.get(Calendar.MINUTE));
                                i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                                getActivity().startActivity(i);
                                dialog.dismiss();
                            }
                        }).create();
    }
}
