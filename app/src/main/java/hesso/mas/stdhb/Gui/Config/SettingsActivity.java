package hesso.mas.stdhb.Gui.Config;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Models.Basemodel;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.05.2016.
 *
 * Activity for the settings
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    private String lComboBoxArray[];

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().

     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a View
        setContentView(R.layout.activity_setting);

        // Similar to another answer, but you can use an ArrayAdapter to populate based on an Enum class.
        // I would recommend overriding toString in the Enum class to make the values populated in the spinner more
        // user friendly.
        Spinner lCboClientServerCommunication = (Spinner) findViewById(R.id.Spinner01);

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumClientServerCommunication.values());

        lCboClientServerCommunication.setAdapter(lAdapter);

        EditText mRayon = (EditText)findViewById(R.id.mDTxtRadius);

        Preferences lPrefs = new Preferences(this);

        String lClientServerCommunication =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_ClientServer_Communication,
                        MyString.EMPTY_STRING);

        EnumClientServerCommunication lEnumValue =
                EnumClientServerCommunication.valueOf(lClientServerCommunication);

        lCboClientServerCommunication.setSelection(lEnumValue.showValue());

        Integer lRaySearch = lPrefs.getPrefValue(BaseConstants.Attr_Search_Radius, Basemodel.NULL_KEY);

        if (lRaySearch.equals(Basemodel.NULL_KEY)) {
            lPrefs.setValue(BaseConstants.Attr_Search_Radius, BaseConstants.Attr_Default_Ray_Search);
            mRayon.setText(BaseConstants.Attr_Default_Ray_Search);
        } else {
            mRayon.setText(lRaySearch.toString());
        }

        Boolean lRadarMode = lPrefs.getBooleanPrefValue(BaseConstants.Attr_Radar_Switch, false);
        Switch lRadarSwitch = (Switch)findViewById(R.id.RadarSwitch);

        lRadarSwitch.setChecked(lRadarMode);
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     *
     */
    public void onClick(View view){}

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume() method.
     *
     *      Be aware that the system calls this method every time your activity comes into the foreground,
     *      including when it's created for the first time. As such, you should implement onResume() to initialize
     *      components that you release during onPause() and perform any other initializations that must occur each time
     *      the activity enters the Resumed state (such as begin animations and initialize components only used while
     *      the activity has user focus).
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * When the system calls onPause() for your activity, it technically means your activity is still
     * partially visible, but most often is an indication that the user is leaving the activity and it
     * will soon enter the Stopped state. You should usually use the onPause() callback to:

             - Check if the activity is visible; if it is not, stop animations or other ongoing actions that could consume CPU.
               Remember, beginning with Android 7.0, a paused app might be running in multi-window mode. In this case, you would
               not want to stop animations or video playback.
             - Commit unsaved changes, but only if users expect such changes to be permanently saved when they leave
               (such as a draft email).
             - Release system resources, such as broadcast receivers, handles to sensors (like GPS), or any resources
               that may affect battery life while your activity is paused and the user does not need them.
     */
    @Override
    public void onPause() {
        super.onPause();

        EditText lRaySearch = (EditText)findViewById(R.id.mDTxtRadius);

        Preferences lPrefs = new Preferences(this);
        Integer lRay = Integer.parseInt(lRaySearch.getText().toString());

        Switch lRadarMode = (Switch)findViewById(R.id.RadarSwitch);
        Boolean lMode = lRadarMode.isChecked();

        Spinner lCboCommunication = (Spinner) findViewById(R.id.Spinner01);
        String lClientServerCommunication = lCboCommunication.getSelectedItem().toString();

        lPrefs.setValue(
                BaseConstants.Attr_Search_Radius,
                lRay);

        lPrefs.setValue(
                BaseConstants.Attr_Radar_Switch,
                lMode);

        lPrefs.setValue(
                BaseConstants.Attr_ClientServer_Communication,
                lClientServerCommunication);
    }
}
