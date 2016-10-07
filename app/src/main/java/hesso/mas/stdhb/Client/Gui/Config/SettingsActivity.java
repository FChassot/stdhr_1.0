package hesso.mas.stdhb.Client.Gui.Config;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Class.CulturalObjectType;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Models.Basemodel;
import hesso.mas.stdhb.Base.Storage.Local.SharedPreferencesLoader;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;
import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask2;
import hesso.mas.stdhb.DataAccess.QueryBuilder.Request.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.CitizenQueryResult;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.05.2016.
 *
 * Activity for the settings
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener,
    LoaderManager.LoaderCallbacks<SharedPreferences> {

    private Receiver mReceiver;

    MyCustomAdapter mDataAdapter = null;

    ArrayList<CulturalObjectType> mCulturalObjectTypes = null;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().
     *
     * @param savedInstanceState ...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a View
        setContentView(R.layout.activity_setting);

        mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask2.ACTION2);
        this.registerReceiver(mReceiver, lFilter);

        // Similar to another answer, but you can use an ArrayAdapter to populate based on an Enum class.
        // I would recommend overriding toString in the Enum class to make the values populated in the spinner more
        // user friendly.
        Spinner lCboClientServerCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        EditText mRayon = (EditText) findViewById(R.id.mDTxtRadius);
        Switch lRadarSwitch = (Switch) findViewById(R.id.RadarSwitch);

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumClientServerCommunication.values());

        lCboClientServerCommunication.setAdapter(lAdapter);

        /*ComboBoxHandler.fillComboClientServerTechnology(
                lCboClientServerCommunication,
                this,
                android.R.layout.simple_spinner_item);*/

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

        lRadarSwitch.setChecked(lRadarMode);

        startAsyncSearch();

        tryToDisplayListview();
    }

    @Override
    public Loader<SharedPreferences> onCreateLoader(int id, Bundle args) {
        return (new SharedPreferencesLoader(this));
    }

    @Override
    public void onLoadFinished(
            Loader<SharedPreferences> loader,
            SharedPreferences prefs) {

        /*int value = prefs.getInt(KEY, 0);
        value += 1;
        textView.setText(String.valueOf(value));
        // update value
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, value);
        SharedPreferencesLoader.persist(editor);*/
    }

    @Override
    public void onLoaderReset(Loader<SharedPreferences> loader) {
        // NOT used
    }

    //region Listbox-Checkbox

    /**
     *
     */
    private void tryToDisplayListview() {

        mCulturalObjectTypes = getCulturalInterestTypes();

        // create an ArrayAdapter from the String Array
        mDataAdapter =
            new MyCustomAdapter(
                this,
                R.layout.culturalinterest_info,
                mCulturalObjectTypes);

        ListView listView = (ListView) findViewById(R.id.mLstViewCITyp);

        // assign adapter to ListView
        listView.setAdapter(mDataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when clicked, show a toast with the TextView text
                CulturalObjectType CulturalObjectType =
                    (CulturalObjectType) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + CulturalObjectType.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<CulturalObjectType> {

        private ArrayList<CulturalObjectType> mListOfCulturalObjectType;

        public MyCustomAdapter(
                Context context,
                int textViewResourceId,
                ArrayList<CulturalObjectType> aListOfCulturalObjectType) {

            super(context, textViewResourceId, aListOfCulturalObjectType);

            this.mListOfCulturalObjectType = new ArrayList<>();
            this.mListOfCulturalObjectType.addAll(aListOfCulturalObjectType);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(
                int aPosition,
                View aConvertView,
                ViewGroup parent) {

            ViewHolder lHolder;

            Log.v("ConvertView", String.valueOf(aPosition));

            if (aConvertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                aConvertView = vi.inflate(R.layout.culturalinterest_info, null);

                lHolder = new ViewHolder();
                lHolder.code = (TextView) aConvertView.findViewById(R.id.code);
                lHolder.name = (CheckBox) aConvertView.findViewById(R.id.checkBox1);
                aConvertView.setTag(lHolder);

                lHolder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        CheckBox lCheckbox = (CheckBox) aView;
                        CulturalObjectType lCulturalObjectType = (CulturalObjectType) lCheckbox.getTag();
                        lCulturalObjectType.setSelected(lCheckbox.isChecked());
                    }
                });
            } else {
                lHolder = (ViewHolder) aConvertView.getTag();
            }

            Preferences lPrefs = new Preferences(this.getContext());
            Set<String> lSet = lPrefs.getSetPrefValue(BaseConstants.Attr_CulturalInterest_type, null);

            CulturalObjectType llCulturalObjectType = mListOfCulturalObjectType.get(aPosition);
            lHolder.code.setText(" (" + llCulturalObjectType.getCode() + ")");
            lHolder.name.setText(llCulturalObjectType.getName());
            if (lSet != null) {
                for (String aCIType : lSet) {
                    if (llCulturalObjectType.getName().equals(aCIType)) {
                        lHolder.name.setChecked(true);
                    }
                }
            }
            //lHolder.name.setChecked(llCulturalInterestType.isSelected());
            lHolder.name.setTag(llCulturalObjectType);

            return aConvertView;
        }
    }

    //endregion

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

        EditText lRadiusSearch = (EditText)findViewById(R.id.mDTxtRadius);
        Switch lRadarMode = (Switch)findViewById(R.id.RadarSwitch);
        Spinner lCboCommunication = (Spinner) findViewById(R.id.mDcboCommunication);

        Preferences lPrefs = new Preferences(this);

        String lRadiusSearchStr = lRadiusSearch.getText().toString();
        int lRadius;

        if (tryParseInt(lRadiusSearchStr)) {
            lRadius = Integer.parseInt(lRadiusSearchStr);

            lPrefs.setValue(
                    BaseConstants.Attr_Search_Radius,
                    lRadius);
        }
        else {
            Notifications.ShowMessageBox(
                    this,
                    "The radius must be smaller than " + Integer.MAX_VALUE,
                    "Warning",
                    "Ok");
        }

        Boolean lMode = lRadarMode.isChecked();
        String lClientServerCommunication = lCboCommunication.getSelectedItem().toString();

        lPrefs.setValue(
                BaseConstants.Attr_Radar_Switch,
                lMode);

        lPrefs.setValue(
                BaseConstants.Attr_ClientServer_Communication,
                lClientServerCommunication);

        Set<String> lSet = new HashSet<>();

        for (CulturalObjectType aCIType : mCulturalObjectTypes) {
            if (aCIType.isSelected()) {
                lSet.add(aCIType.getName());
            }
        }

        lPrefs.setValue(
                BaseConstants.Attr_CulturalInterest_type,
                lSet);
    }

    /**
     *
     *
     * @param value
     * @return
     */
    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //region AsyncTask (used to search the cultural object Types)

    /**
     * Start an Async search on the endPoint Sparql Server
     *
     */
    private void startAsyncSearch() {

        RetrieveCitizenDataAsyncTask lRetrieveTask =
            new RetrieveCitizenDataAsyncTask(
                this,
                RetrieveCitizenDataAsyncTask.ACTION2);

        Preferences lPrefs = new Preferences(this);

        String lClientServerCommunicationMode =
            lPrefs.getPrefValue(
                BaseConstants.Attr_ClientServer_Communication,
                MyString.EMPTY_STRING);

        EnumClientServerCommunication lEnumValue =
            EnumClientServerCommunication.valueOf(
                lClientServerCommunicationMode);

        if (lEnumValue != EnumClientServerCommunication.ANDROJENA) {
            Notifications.ShowMessageBox(
                    this,
                    getResources().getString(R.string.txt_radar_possible_mode),
                    getResources().getString(R.string.Warning),
                    getResources().getString(R.string.Ok));

            return;
        }

        lRetrieveTask.onPreExecuteMessageDisplay = false;

        String lQuery = CitizenRequests.getCulturalObjectTypeQuery();

        lRetrieveTask.execute(
            lQuery,
            lClientServerCommunicationMode);
    }

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private class Receiver extends BroadcastReceiver {

        /**
         * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
         * During this time you can use the other methods on BroadcastReceiver to view/modify
         * the current result values. This method is always called within the main thread of
         * its process, unless you explicitly asked for it to be scheduled on a different thread
         * using registerReceiver(BroadcastReceiver, IntentFilter, String, android.os.Handler).
         * When it runs on the main thread you should never perform long-running operations in it
         * (there is a timeout of 10 seconds that the system allows before considering the receiver
         * to be blocked and a candidate to be killed). You cannot launch a popup dialog in your
         * implementation of onReceive().
         */
        @Override
        public void onReceive(Context aContext, Intent aIntent)
        {
            Bundle lBundle = aIntent.getExtras();

            CitizenQueryResult lCitizenQueryResult = null;

            try {
                lCitizenQueryResult =
                    lBundle.getParcelable(
                        RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

            } catch (Exception e) {
                Log.i("Settings AsyncTask", e.getMessage());
            }

            mCulturalObjectTypes =
                RadarHelper.getRadarMarkersFromResponse(
                    lCitizenQueryResult);
        }
    }

    /**
     *
     * @return
     */
    private ArrayList<CulturalObjectType> getCulturalInterestTypes() {

        // array list of type of cultural interest
        ArrayList<CulturalObjectType> lListOfCIType = new ArrayList<>();

        lListOfCIType.add(new CulturalObjectType("","Cultural place", false));
        lListOfCIType.add(new CulturalObjectType("","Cultural person", false));
        lListOfCIType.add(new CulturalObjectType("","Cultural event", false));
        lListOfCIType.add(new CulturalObjectType("","Folklore", false));
        lListOfCIType.add(new CulturalObjectType("","Physical object", false));

        return lListOfCIType;
    }

    //endregion
}
