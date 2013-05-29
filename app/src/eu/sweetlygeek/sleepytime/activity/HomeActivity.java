
package eu.sweetlygeek.sleepytime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import eu.sweetlygeek.sleepytime.R;
import eu.sweetlygeek.sleepytime.activity.fragment.AboutDialogFragment;
import eu.sweetlygeek.sleepytime.activity.fragment.TimeDialogFragment;
import eu.sweetlygeek.sleepytime.model.Choice;
import eu.sweetlygeek.sleepytime.utils.SleepUtils;

/**
 * Default activity
 * 
 * @author bishiboosh
 */
public class HomeActivity extends SherlockFragmentActivity {

    private void openTimePicker(Choice choice) {
        Bundle args = new Bundle();
        args.putSerializable(TimeDialogFragment.CHOICE_KEY, choice);
        TimeDialogFragment fragment = new TimeDialogFragment();
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button chooseTime = (Button) findViewById(R.id.choose_time);
        chooseTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openTimePicker(Choice.CHOOSE_WAKE);
            }
        });
        Button chooseBedTime = (Button) findViewById(R.id.choose_bed_time);
        chooseBedTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openTimePicker(Choice.CHOOSE_BED);
            }
        });
        Button zzz = (Button) findViewById(R.id.zzz);
        zzz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ResultsActivity.class);
                i.putExtra(SleepUtils.CHOICE_PARAM, Choice.BED_NOW);
                startActivity(i);
            }
        });
        Button about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AboutDialogFragment().show(getSupportFragmentManager(), "aboutDialog");
            }
        });
    }
}
