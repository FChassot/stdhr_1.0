package hesso.mas.stdhb.Client.Gui.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
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
import java.util.List;

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

import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCityZenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.SparqlRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.05.2016.
 *
 * Activity for the settings
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    // Constant
    private static final String TAG = "Config AsyncTask";

    public static final String AsyncTaskAction = "Search_for_Config";

    // Member variables
    private Preferences mPrefs;

    private ListViewAdapter mListViewAdapter = null;

    private NetworkConnectivity mConnectivity;

    private ArrayList<CulturalObjectType> mCulturalObjectTypes = null;

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

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_config);

        // Declares a broadcast receiver as one of the application's components.
        // Broadcast receivers enable applications to receive intents that are
        // broadcast by the system or by other applications, even when other
        // components of the application are not running.
        Receiver mReceiver = new Receiver();

        IntentFilter filter = new IntentFilter(AsyncTaskAction);
        this.registerReceiver(mReceiver, filter);

        mPrefs = new Preferences(this);

        mConnectivity = new NetworkConnectivity(this);

        // Find the views that was identified by an id attribute
        Spinner lSpinnerClientServerCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        EditText mDTxtRadius = (EditText) findViewById(R.id.mDTxtRadius);
        Switch lRadarSwitch = (Switch) findViewById(R.id.RadarSwitch);

        lRadarSwitch.setFocusable(true);

        // Similar to another answer, but you can use an ArrayAdapter to populate based on an Enum class.
        ArrayAdapter adapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumClientServerCommunication.values());

        adapter.setDropDownViewResource(R.layout.spinner_item);

        lSpinnerClientServerCommunication.setAdapter(adapter);

        /*SpinnerHandler.fillComboClientServerTechnology(
                lCboClientServerCommunication,
                this,
                android.R.layout.simple_spinner_item);*/

        String clientServerCommunication =
                mPrefs.getMyStringPref(
                        this,
                        BaseConstants.Attr_ClientServer_Communication,
                        EnumClientServerCommunication.ANDROJENA.toString());

        EnumClientServerCommunication lEnumValue =
                EnumClientServerCommunication.valueOf(
                        clientServerCommunication);

        lSpinnerClientServerCommunication.setSelection(lEnumValue.showValue());

        Integer radiusOfSearch =
                mPrefs.getMyIntPref(
                        this,
                        BaseConstants.Attr_Radius_Search,
                        BaseConstants.Attr_Default_Radius_Search);

        mDTxtRadius.setText(radiusOfSearch.toString());

        Boolean radarMode = mPrefs.getMyBooleanPref(this, BaseConstants.Attr_Radar_Switch, true);
        lRadarSwitch.setChecked(radarMode);

        if (mConnectivity.isActive() && mConnectivity.isNetworkAvailable()) {
            startAsyncSearch();
        }
        else {
            Spinner subjectSpinner = (Spinner) findViewById(R.id.mDcboSubject);

            List<String> culturalObjectSubjects = new ArrayList<>();

            String subjectSelected =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_Subject_Search_Type,
                            MyString.EMPTY_STRING);

            culturalObjectSubjects.add(subjectSelected);

            ArrayAdapter<String> subjectAdapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            culturalObjectSubjects);

            subjectAdapter.setDropDownViewResource(R.layout.spinner_item);
            subjectSpinner.setAdapter(subjectAdapter);
            subjectSpinner.setSelection(0);
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
        mListViewAdapter =
            new ListViewAdapter(
                this,
                R.layout.cultural_interest_info,
                mCulturalObjectTypes);

        ListView listView = (ListView) findViewById(R.id.mLstViewCITyp);

        // Assign the adapter to the ListView
        listView.setAdapter(mListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
                // when clicked, show a toast with the TextView text
                CulturalObjectType culturalObjectType =
                        (CulturalObjectType) aParent.getItemAtPosition(aPosition);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + culturalObjectType.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Class listViewAdapter
     */
    private class ListViewAdapter extends ArrayAdapter<CulturalObjectType> {

        // Member variable
        private ArrayList<CulturalObjectType> mListOfCulturalObjectType;

        /**
         * Constructor
         *
         * @param context
         * @param textViewResourceId
         * @param listOfCulturalObjectType
         */
        public ListViewAdapter(
                Context context,
                int textViewResourceId,
                ArrayList<CulturalObjectType> listOfCulturalObjectType) {

            super(context, textViewResourceId, listOfCulturalObjectType);

            this.mListOfCulturalObjectType = new ArrayList<>();
            this.mListOfCulturalObjectType.addAll(listOfCulturalObjectType);
        }

        /**
         * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
         */
        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        /**
         * Get a View that displays the data at the specified position in the data set.
         *
         * @param position The position of the item within the adapter's data set of the item whose
         *                  view we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this
         *                     view is non-null and of an appropriate type before using. If it is
         *                     not possible to convert this view to display the correct data,
         *                     this method can create a new view. Heterogeneous lists can specify
         *                     their number of view types, so that this View is always of the right
         *                     type (see getViewTypeCount() and getItemViewType(int)).
         * @param parent The parent that this view will eventually be attached to
         *
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            ViewHolder holder;

            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater lLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = lLayoutInflater.inflate(R.layout.cultural_interest_info, null);

                holder = new ViewHolder();

                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        CheckBox lCheckbox = (CheckBox) aView;
                        CulturalObjectType lCulturalObjectType = (CulturalObjectType) lCheckbox.getTag();
                        lCulturalObjectType.setSelected(lCheckbox.isChecked());
                        int lIndex = 0;

                        for (CulturalObjectType culturalObjType : mCulturalObjectTypes) {
                            if (culturalObjType.getName().equals(lCulturalObjectType.getName())) {
                                break;
                            }
                            else {
                                lIndex += 1;
                            }
                        }

                        if (mCulturalObjectTypes != null) {
                            mCulturalObjectTypes.add(lIndex, lCulturalObjectType);
                        }
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            CulturalObjectType culturalObjectType = mListOfCulturalObjectType.get(position);

            holder.code.setText(" (" + culturalObjectType.getCode() + ")");
            holder.name.setText(culturalObjectType.getName());
            holder.name.setChecked(culturalObjectType.isSelected());
            holder.name.setTag(culturalObjectType);

            return convertView;
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

        // Finds the views that was identified by an id attribute
        EditText txtRadius = (EditText)findViewById(R.id.mDTxtRadius);
        Switch switchRadar = (Switch)findViewById(R.id.RadarSwitch);
        Spinner cboCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        Spinner cboSubject = (Spinner) findViewById(R.id.mDcboSubject);

        String radiusSearchStr = txtRadius.getText().toString();

        if (IntegerUtil.tryParseInt(radiusSearchStr)) {
            mPrefs.setMyIntPref(
                    this,
                    BaseConstants.Attr_Radius_Search,
                    Integer.parseInt(radiusSearchStr));
        }
        else {
            Notifications.ShowMessageBox(
                    this,
                    "The radius must be smaller than " + Integer.MAX_VALUE,
                    "Warning",
                    "Ok");
        }

        if(cboSubject != null && cboSubject.getSelectedItem() !=null ) {
            String value = cboSubject.getSelectedItem().toString();

            if (!value.equals(MyString.EMPTY_STRING)) {
                mPrefs.setMyStringPref(
                        this,
                        BaseConstants.Attr_Subject_Search_Type,
                        cboSubject.getSelectedItem().toString());
            }
        }

        Boolean radarMode = switchRadar.isChecked();

        mPrefs.setMyBooleanPref(
                this,
                BaseConstants.Attr_Radar_Switch,
                radarMode);

        String clientServerCommunication = cboCommunication.getSelectedItem().toString();

        mPrefs.setMyStringPref(
                this,
                BaseConstants.Attr_ClientServer_Communication,
                clientServerCommunication);

        List<String> listOfCulturalObjectType = new ArrayList<>();

        for (CulturalObjectType aCulturalObjectType : mCulturalObjectTypes) {
            if (aCulturalObjectType.isSelected()) {
                listOfCulturalObjectType.add(aCulturalObjectType.getName());
            }
        }

        setCulturalObjectTypeInPrefs(mCulturalObjectTypes);

        mPrefs.setMyStringPref(
                this,
                BaseConstants.Attr_Subject_Search_Type,
                cboSubject.getSelectedItem().toString());
    }

    //region AsyncTask (used to search the cultural object Types)

    /**
     * Start an Async search on the endPoint Sparql Server
     */
    private void startAsyncSearch() {

        RetrieveCityZenDataAsyncTask retrieveTask =
            new RetrieveCityZenDataAsyncTask(
                this,
                AsyncTaskAction);

        String clientServerCommunicationMode =
            mPrefs.getMyStringPref(
                this,
                BaseConstants.Attr_ClientServer_Communication,
                EnumClientServerCommunication.ANDROJENA.toString());

        EnumClientServerCommunication enumValue =
            EnumClientServerCommunication.valueOf(
                clientServerCommunicationMode);

        if (enumValue != EnumClientServerCommunication.ANDROJENA) {
            Notifications.ShowMessageBox(
                    this,
                    getResources().getString(R.string.txt_radar_possible_mode),
                    getResources().getString(R.string.Warning),
                    getResources().getString(R.string.Ok));

            return;
        }

        retrieveTask.onPreExecuteMessageDisplay = false;

        //String lQuery = SparqlRequests.getCulturalObjectTypeQuery();
        String query = SparqlRequests.getSubjectQuery();

        retrieveTask.execute(
            query,
            clientServerCommunicationMode);
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
        public void onReceive(Context context, Intent intent)
        {
            if (!intent.getAction().equals(AsyncTaskAction)) {
                return;
            }

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle bundle = intent.getExtras();

            CityZenQueryResult cityzenQueryResult = null;

            try {
                cityzenQueryResult =
                    bundle.getParcelable(AsyncTaskAction);
            }
            catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            List<String> culturalObjectSubjects =
                    RadarHelper.getCulturalObjectSubjectFromResponse(
                        cityzenQueryResult);

            Spinner subjectSpinner = (Spinner) findViewById(R.id.mDcboSubject);

            ArrayAdapter<String> subjectAdapter =
                    new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            culturalObjectSubjects);

            subjectAdapter.setDropDownViewResource(R.layout.spinner_item);
            subjectSpinner.setAdapter(subjectAdapter);

            String subjectSelected =
                    mPrefs.getMyStringPref(
                            context,
                            BaseConstants.Attr_Subject_Search_Type,
                            MyString.EMPTY_STRING);

            Integer itemPosition =
                    SpinnerHandler.getPositionByItem(subjectSpinner, subjectSelected);

            if (!itemPosition.equals(Basemodel.NULL_KEY)) {
                subjectSpinner.setSelection(itemPosition);
            }
        }
    }

    /**
     * This method persist the cultural object types choosen in the settings
     * @return
     */
    private void setCulturalObjectTypeInPrefs(
         List<CulturalObjectType> listCulturalObjectType) {

        for (CulturalObjectType culturalObjectType : listCulturalObjectType) {
            String lAttributeName =
                    getCorrespondingAttributeName(culturalObjectType.getName());

            if (culturalObjectType.isSelected()) {
                mPrefs.setMyStringPref(
                        this,
                        lAttributeName,
                        "1");
            } else {
                mPrefs.setMyStringPref(
                        this,
                        lAttributeName,
                        "0");
            }
        }
    }

    /**
     * Give the list of cultural interest to fill in the ListView
     */
    private ArrayList<CulturalObjectType> getCulturalInterestTypes() {

        // array list of type of cultural interest
        ArrayList<CulturalObjectType> listOfCIType = new ArrayList<>();

        listOfCIType.add(getCulturalObjectTypeAccordingToPrefs("Cultural place", "1"));
        listOfCIType.add(getCulturalObjectTypeAccordingToPrefs("Cultural person", "2"));
        listOfCIType.add(getCulturalObjectTypeAccordingToPrefs("Cultural event", "3"));
        listOfCIType.add(getCulturalObjectTypeAccordingToPrefs("Folklore", "4"));
        listOfCIType.add(getCulturalObjectTypeAccordingToPrefs("Physical object", "5"));

        return listOfCIType;
    }

    /**
     *
     * @param aCulturalInterestName
     * @param code
     * @return
     */
    private CulturalObjectType getCulturalObjectTypeAccordingToPrefs(
        String aCulturalInterestName,
        String code) {

        String lAttributeName =
                getCorrespondingAttributeName(aCulturalInterestName);

        String culturalObjectValue =
                mPrefs.getMyStringPref(
                        this,
                        lAttributeName,
                        "1");

        if (!culturalObjectValue.equals(MyString.EMPTY_STRING) && culturalObjectValue == "1") {
            return new CulturalObjectType(code, aCulturalInterestName, true);
        }
        else {
            return new CulturalObjectType(code, aCulturalInterestName, false);
        }
    }

    /**
     *
     * @param culturalObjectType
     *
     * @return
     */
    private String getCorrespondingAttributeName(String culturalObjectType) {

        if (culturalObjectType.equals("Cultural place")) {
            return BaseConstants.Attr_CulturalPlace;
        }
        if (culturalObjectType.equals("Cultural person")) {
            return BaseConstants.Attr_CulturalPerson;
        }
        if (culturalObjectType.equals("Cultural event")) {
            return BaseConstants.Attr_CulturalEvent;
        }
        if (culturalObjectType.equals("Folklore")) {
            return BaseConstants.Attr_Folklore;
        }
        if (culturalObjectType.equals("Physical object")) {
            return BaseConstants.Attr_PhysicalObject;
        }

        return MyString.EMPTY_STRING;
    }

    //endregion
}
