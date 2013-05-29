/**
 * 
 */

package eu.sweetlygeek.sleepytime.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import eu.sweetlygeek.sleepytime.R;

/**
 * About dialog
 * 
 * @author valentin.rocher
 */
public class AboutDialogFragment extends SherlockDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage(R.string.about_text).create();
    }

}
