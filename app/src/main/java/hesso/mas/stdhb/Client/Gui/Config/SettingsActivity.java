package hesso.mas.stdhb.Client.Gui.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
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

import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Tools.SpinnerHandler;

import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.05.2016.
 *
 * Activity for the settings
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    // Constant
    private static final String TAG = "Config AsyncTask";

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

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_setting);

        // Declares a broadcast receiver as one of the application's components.
        // Broadcast receivers enable applications to receive intents that are
        // broadcast by the system or by other applications, even when other
        // components of the application are not running.
        Receiver mReceiver = new Receiver();

        IntentFilter filter = new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION3);
        this.registerReceiver(mReceiver, filter);

        mPrefs = new Preferences(this);

        mConnectivity = new NetworkConnectivity(this);

        // Find the views that was identified by an id attribute
        Spinner lCboClientServerCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
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

        lCboClientServerCommunication.setAdapter(adapter);

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

        lCboClientServerCommunication.setSelection(lEnumValue.showValue());

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
                            BaseConstants.Attr_Subject_Selected,
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
                R.layout.culturalinterest_info,
                mCulturalObjectTypes);

        ListView listView = (ListView) findViewById(R.id.mLstViewCITyp);

        // Assign the adapter to the ListView
        listView.setAdapter(mListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
                // when clicked, show a toast with the TextView text
                CulturalObjectType CulturalObjectType = (CulturalObjectType) aParent.getItemAtPosition(aPosition);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + CulturalObjectType.getName(),
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

                convertView = lLayoutInflater.inflate(R.layout.culturalinterest_info, null);

                holder = new ViewHolder();

                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        CheckBox lCheckbox = (CheckBox) aView;
                        CulturalObjectType lCulturalObjectType = (CulturalObjectType) lCheckbox.getTag();
                        lCulturalObjectType.setSelected(lCheckbox.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Set<String> lListOfCulturalObjectType =
                    mPrefs.getMySetPref(
                        this.getContext(),
                        BaseConstants.Attr_CulturalObject_Type,
                        null);

            CulturalObjectType llCulturalObjectType = mListOfCulturalObjectType.get(position);

            holder.code.setText(" (" + llCulturalObjectType.getCode() + ")");
            holder.name.setText(llCulturalObjectType.getName());

            if (lListOfCulturalObjectType != null) {
                for (String aCulturalObjectType : lListOfCulturalObjectType) {
                    if (llCulturalObjectType.getName().equals(aCulturalObjectType)) {
                        holder.name.setChecked(true);
                    }
                }
            }
            //lHolder.name.setChecked(llCulturalInterestType.isSelected());
            holder.name.setTag(llCulturalObjectType);

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

        Set<String> listOfCulturalObjectType = new HashSet<>();

        for (CulturalObjectType aCulturalObjectType : mCulturalObjectTypes) {
            if (aCulturalObjectType.isSelected()) {
                listOfCulturalObjectType.add(aCulturalObjectType.getName());
            }
        }

        mPrefs.setMySetPref(
                this,
                BaseConstants.Attr_CulturalObject_Type,
                listOfCulturalObjectType);

        mPrefs.setMyStringPref(
                this,
                BaseConstants.Attr_Subject_Selected,
                cboSubject.getSelectedItem().toString());
    }

    //region AsyncTask (used to search the cultural object Types)

    /**
     * Start an Async search on the endPoint Sparql Server
     */
    private void startAsyncSearch() {

        RetrieveCitizenDataAsyncTask retrieveTask =
            new RetrieveCitizenDataAsyncTask(
                this,
                RetrieveCitizenDataAsyncTask.ACTION3);

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

        //String lQuery = CitizenRequests.getCulturalObjectTypeQuery();
        String query = CitizenRequests.getSubjectQuery();

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
            if (!intent.getAction().equals(RetrieveCitizenDataAsyncTask.ACTION3)) {
                return;
            }

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle bundle = intent.getExtras();

            CitizenQueryResult citizenQueryResult = null;

            try {
                citizenQueryResult =
                    bundle.getParcelable(
                        RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);
            }
            catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            List<String> culturalObjectSubjects =
                    RadarHelper.getCulturalObjectSubjectFromResponse(
                        citizenQueryResult);

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
                            BaseConstants.Attr_Subject_Selected,
                            MyString.EMPTY_STRING);

            Integer itemPosition =
                    SpinnerHandler.getPositionByItem(subjectSpinner, subjectSelected);

            if (!itemPosition.equals(Basemodel.NULL_KEY)) {
                subjectSpinner.setSelection(itemPosition);
            }
        }
    }

    /**
     *
     */
    private ArrayList<CulturalObjectType> getCulturalInterestTypes() {

        // array list of type of cultural interest
        ArrayList<CulturalObjectType> listOfCIType = new ArrayList<>();

        listOfCIType.add(new CulturalObjectType("","Cultural place", false));
        listOfCIType.add(new CulturalObjectType("","Cultural person", false));
        listOfCIType.add(new CulturalObjectType("","Cultural event", false));
        listOfCIType.add(new CulturalObjectType("","Folklore", false));
        listOfCIType.add(new CulturalObjectType("","Physical object", false));

        return listOfCIType;
    }

    //endregion
}
