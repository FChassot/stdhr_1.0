package hesso.mas.stdhb.Client.Gui.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.List;
import java.util.Set;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Class.CulturalObjectType;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Models.Basemodel;
import hesso.mas.stdhb.Base.Tools.IntegerUtil;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Tools.SpinnerHandler;

import hesso.mas.stdhb.DataAccess.Communication.Services.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.05.2016.
 *
 * Activity for the settings
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    // Member variables
    private Preferences mPrefs;

    //private Receiver mReceiver;

    private MyCustomAdapter mDataAdapter = null;

    private NetworkConnectivity mConnectivity;

    private ArrayList<CulturalObjectType> mCulturalObjectTypes = null;

    private List<String> mCulturalObjectSubjects = new ArrayList<>();

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

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_setting);

        // Declares a broadcast receiver as one of the application's components.
        // Broadcast receivers enable applications to receive intents that are
        // broadcast by the system or by other applications, even when other
        // components of the application are not running.
        Receiver mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION3);
        this.registerReceiver(mReceiver, lFilter);

        mPrefs = new Preferences(this);

        mConnectivity = new NetworkConnectivity(this);

        // Finds the views that was identified by an id attribute
        Spinner lCboClientServerCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        EditText mDTxtRadius = (EditText) findViewById(R.id.mDTxtRadius);
        Switch lRadarSwitch = (Switch) findViewById(R.id.RadarSwitch);

        lRadarSwitch.setFocusable(true);

        // Similar to another answer, but you can use an ArrayAdapter to populate based on an Enum class.
        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumClientServerCommunication.values());

        lCboClientServerCommunication.setAdapter(lAdapter);

        /*SpinnerHandler.fillComboClientServerTechnology(
                lCboClientServerCommunication,
                this,
                android.R.layout.simple_spinner_item);*/

        String lClientServerCommunication =
                mPrefs.getMyStringPref(
                        this,
                        BaseConstants.Attr_ClientServer_Communication,
                        EnumClientServerCommunication.ANDROJENA.toString());

        EnumClientServerCommunication lEnumValue =
                EnumClientServerCommunication.valueOf(lClientServerCommunication);

        lCboClientServerCommunication.setSelection(lEnumValue.showValue());

        Integer lRadiusOfSearch =
                mPrefs.getMyIntPref(
                        this,
                        BaseConstants.Attr_Radius_Search,
                        BaseConstants.Attr_Default_Radius_Search);

        mDTxtRadius.setText(lRadiusOfSearch.toString());

        Boolean lRadarMode = mPrefs.getMyBooleanPref(this, BaseConstants.Attr_Radar_Switch, false);
        lRadarSwitch.setChecked(lRadarMode);

        if (mConnectivity.isActive()) {
            startAsyncSearch();
        }
        else {
            Spinner lSubjectSpinner = (Spinner) findViewById(R.id.mDcboSubject);

            List<String> lCulturalObjectSubjects = new ArrayList<>();

            String lSubjectSelected =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_Subject_Selected,
                            MyString.EMPTY_STRING);

            lCulturalObjectSubjects.add(lSubjectSelected);

            ArrayAdapter<String> lSubjectAdapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            lCulturalObjectSubjects);

            lSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lSubjectSpinner.setAdapter(lSubjectAdapter);
            lSubjectSpinner.setSelection(0);
        }

        displayListView();

    }

    //region Listview (with Checkboxes)

    /**
     * This method allows to display a listview
     */
    private void displayListView() {

        mCulturalObjectTypes = getCulturalInterestTypes();

        // Create an ArrayAdapter from the String Array
        mDataAdapter =
            new MyCustomAdapter(
                this,
                R.layout.culturalinterest_info,
                mCulturalObjectTypes);

        ListView listView = (ListView) findViewById(R.id.mLstViewCITyp);

        // Assign the adapter to the ListView
        listView.setAdapter(mDataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when clicked, show a toast with the TextView text
                CulturalObjectType CulturalObjectType = (CulturalObjectType) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + CulturalObjectType.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Class myCustomAdapter
     */
    private class MyCustomAdapter extends ArrayAdapter<CulturalObjectType> {

        // Member variable
        private ArrayList<CulturalObjectType> mListOfCulturalObjectType;

        /**
         *
         * @param context
         * @param textViewResourceId
         * @param aListOfCulturalObjectType
         */
        public MyCustomAdapter(
                Context context,
                int textViewResourceId,
                ArrayList<CulturalObjectType> aListOfCulturalObjectType) {

            super(context, textViewResourceId, aListOfCulturalObjectType);

            this.mListOfCulturalObjectType = new ArrayList<>();
            this.mListOfCulturalObjectType.addAll(aListOfCulturalObjectType);
        }

        /**
         *
         */
        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        /**
         *
         * @param aPosition
         * @param aConvertView
         * @param parent
         *
         * @return Returns the view
         */
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
            }
            else {
                lHolder = (ViewHolder) aConvertView.getTag();
            }

            //Preferences lPrefs = new Preferences(this.getContext());

            Set<String> lListOfCulturalObjectType =
                    mPrefs.getMySetPref(
                        this.getContext(),
                        BaseConstants.Attr_CulturalObject_Type,
                        null);

            CulturalObjectType llCulturalObjectType = mListOfCulturalObjectType.get(aPosition);

            lHolder.code.setText(" (" + llCulturalObjectType.getCode() + ")");
            lHolder.name.setText(llCulturalObjectType.getName());

            if (lListOfCulturalObjectType != null) {
                for (String aCulturalObjectType : lListOfCulturalObjectType) {
                    if (llCulturalObjectType.getName().equals(aCulturalObjectType)) {
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
     */
    public void onClick(View view){}

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume()
     * method.
     *
     * Be aware that the system calls this method every time your activity comes into the foreground,
     * including when it's created for the first time. As such, you should implement onResume() to
     * initialize components that you release during onPause() and perform any other initializations
     * that must occur each time the activity enters the Resumed state (such as begin animations and
     * initialize components only used while the activity has user focus).
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     * This is typically used to commit unsaved changes to persistent data, stop animations and
     * other things that may be consuming CPU, etc. Implementations of this method must be very
     * quick because the next activity will not be resumed until this method returns.
     * Followed by either onResume() if the activity returns back to the front, or onStop() if it
     * becomes invisible to the user.
     */
    @Override
    public void onPause() {
        super.onPause();

        EditText lTxtRadius = (EditText)findViewById(R.id.mDTxtRadius);
        Switch lSwitchRadar = (Switch)findViewById(R.id.RadarSwitch);
        Spinner lCboCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        Spinner lCboSubject = (Spinner) findViewById(R.id.mDcboSubject);

        String lRadiusSearchStr = lTxtRadius.getText().toString();

        if (IntegerUtil.tryParseInt(lRadiusSearchStr)) {
            mPrefs.setMyIntPref(
                    this,
                    BaseConstants.Attr_Radius_Search,
                    Integer.parseInt(lRadiusSearchStr));
        }
        else {
            Notifications.ShowMessageBox(
                    this,
                    "The radius must be smaller than " + Integer.MAX_VALUE,
                    "Warning",
                    "Ok");
        }

        Boolean lRadarMode = lSwitchRadar.isChecked();
        String lClientServerCommunication = lCboCommunication.getSelectedItem().toString();

        if (lCboSubject.getSelectedItem() != null){
            mPrefs.setMyStringPref(
                    this,
                    BaseConstants.Attr_Subject_Search_Type,
                    lCboSubject.getSelectedItem().toString());
        }

        mPrefs.setMyBooleanPref(
                this,
                BaseConstants.Attr_Radar_Switch,
                lRadarMode);

        mPrefs.setMyStringPref(
                this,
                BaseConstants.Attr_ClientServer_Communication,
                lClientServerCommunication);

        Set<String> lListOfCulturalObjectType = new HashSet<>();

        for (CulturalObjectType aCulturalObjectType : mCulturalObjectTypes) {
            if (aCulturalObjectType.isSelected()) {
                lListOfCulturalObjectType.add(aCulturalObjectType.getName());
            }
        }

        mPrefs.setMySetPref(
                this,
                BaseConstants.Attr_CulturalObject_Type,
                lListOfCulturalObjectType);

        String lSubjectSelected = lCboSubject.getSelectedItem().toString();

        mPrefs.setMyStringPref(
                this,
                BaseConstants.Attr_Subject_Selected,
                lSubjectSelected);
    }

    //region AsyncTask (used to search the cultural object Types)

    /**
     * Start an Async search on the endPoint Sparql Server
     */
    private void startAsyncSearch() {

        RetrieveCitizenDataAsyncTask lRetrieveTask =
            new RetrieveCitizenDataAsyncTask(
                this,
                RetrieveCitizenDataAsyncTask.ACTION3);

        String lClientServerCommunicationMode =
            mPrefs.getMyStringPref(
                this,
                BaseConstants.Attr_ClientServer_Communication,
                EnumClientServerCommunication.ANDROJENA.toString());

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

        //String lQuery = CitizenRequests.getCulturalObjectTypeQuery();
        String lQuery = CitizenRequests.getSubjectQuery();

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
            if (!aIntent.getAction().equals(RetrieveCitizenDataAsyncTask.ACTION3)) {
                return;
            }

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle lBundle = aIntent.getExtras();

            CitizenQueryResult lCitizenQueryResult = null;

            try {
                lCitizenQueryResult =
                    lBundle.getParcelable(
                        RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);
            }
            catch (Exception aExc) {
                Log.i("Config AsyncTask", aExc.getMessage());
            }

            List<String> lCulturalObjectSubjects =
                    RadarHelper.getCulturalObjectSubjectFromResponse(
                            lCitizenQueryResult);

            Spinner lSubjectSpinner = (Spinner) findViewById(R.id.mDcboSubject);

            ArrayAdapter<String> lSubjectAdapter =
                    new ArrayAdapter<>(
                            aContext,
                            android.R.layout.simple_spinner_item,
                            lCulturalObjectSubjects);

            lSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lSubjectSpinner.setAdapter(lSubjectAdapter);

            String lSubjectSelected =
                    mPrefs.getMyStringPref(
                            aContext,
                            BaseConstants.Attr_Subject_Selected,
                            MyString.EMPTY_STRING);

            Integer lItemPosition = SpinnerHandler.getPositionByItem(lSubjectSpinner, lSubjectSelected);

            if (!lItemPosition.equals(Basemodel.NULL_KEY))
            {
                lSubjectSpinner.setSelection(lItemPosition);
            }
        }
    }

    /**
     *
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
