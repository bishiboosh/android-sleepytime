/**
 * 
 */

package eu.sweetlygeek.sleepytime.activity.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockDialogFragment;

import eu.sweetlygeek.sleepytime.activity.ResultsActivity;
import eu.sweetlygeek.sleepytime.model.Choice;
import eu.sweetlygeek.sleepytime.utils.SleepUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Time dialog
 * 
 * @author valentin.rocher
 */
public class TimeDialogFragment extends SherlockDialogFragment {

    public final static String CHOICE_KEY = "choice";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Choice choice = (Choice) getArguments().getSerializable(CHOICE_KEY);
        String language = Locale.getDefault().getLanguage();
        boolean is24h = Locale.FRENCH.getLanguage().equals(language);
        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                goToNext(calendar.getTime(), choice);
            }
        }, 0, 0, is24h);
    }

    private void goToNext(Date date, Choice choice) {
        Intent i = new Intent(getActivity(), ResultsActivity.class);
        i.putExtra(SleepUtils.DATE_PARAM, date);
        i.putExtra(SleepUtils.CHOICE_PARAM, choice);
        if (i != null && choice != null) {
            startActivity(i);
        }
    }
}
