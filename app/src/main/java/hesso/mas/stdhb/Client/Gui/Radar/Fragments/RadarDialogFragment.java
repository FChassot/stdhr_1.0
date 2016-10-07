package hesso.mas.stdhb.Client.Gui.Radar.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by chf on 07.10.2016.
 */
public class RadarDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle aSavedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(getActivity());

        lBuilder.setMessage("Choose an action!")
                .setPositiveButton("Google Map", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Citizen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return lBuilder.create();
    }
}