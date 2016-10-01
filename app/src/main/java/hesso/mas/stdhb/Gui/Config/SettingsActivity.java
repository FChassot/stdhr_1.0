package hesso.mas.stdhb.Gui.Config;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.CulturalInterestType;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Models.Enum.EnumCulturalInterestType;
import hesso.mas.stdhb.Base.Notifications.Notifications;
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

    private String mComboBoxArray[];

    MyCustomAdapter mDataAdapter = null;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a View
        setContentView(R.layout.activity_setting);

        displayListView();

        // Similar to another answer, but you can use an ArrayAdapter to populate based on an Enum class.
        // I would recommend overriding toString in the Enum class to make the values populated in the spinner more
        // user friendly.
        Spinner lCboClientServerCommunication = (Spinner) findViewById(R.id.mDcboCommunication);
        Spinner lCboCulturalInterestType = (Spinner) findViewById(R.id.mDcboCulturalInterestType);
        EditText mRayon = (EditText) findViewById(R.id.mDTxtRadius);
        Switch lRadarSwitch = (Switch) findViewById(R.id.RadarSwitch);

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumClientServerCommunication.values());

        ArrayAdapter lCIAdapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        EnumCulturalInterestType.values());

        lCboClientServerCommunication.setAdapter(lAdapter);
        lCboCulturalInterestType.setAdapter(lCIAdapter);

        Preferences lPrefs = new Preferences(this);

        String lClientServerCommunication =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_ClientServer_Communication,
                        MyString.EMPTY_STRING);

        EnumClientServerCommunication lEnumValue =
                EnumClientServerCommunication.valueOf(lClientServerCommunication);

        lCboClientServerCommunication.setSelection(lEnumValue.showValue());

        String lCIType =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_CulturalInterest_type,
                        MyString.EMPTY_STRING);

        EnumCulturalInterestType lEnumCITypeValue =
                EnumCulturalInterestType.valueOf(lCIType);

        lCboCulturalInterestType.setSelection(lEnumCITypeValue.showValue());

        Integer lRaySearch = lPrefs.getPrefValue(BaseConstants.Attr_Search_Radius, Basemodel.NULL_KEY);

        if (lRaySearch.equals(Basemodel.NULL_KEY)) {
            lPrefs.setValue(BaseConstants.Attr_Search_Radius, BaseConstants.Attr_Default_Ray_Search);
            mRayon.setText(BaseConstants.Attr_Default_Ray_Search);
        } else {
            mRayon.setText(lRaySearch.toString());
        }

        Boolean lRadarMode = lPrefs.getBooleanPrefValue(BaseConstants.Attr_Radar_Switch, false);

        lRadarSwitch.setChecked(lRadarMode);
    }

    //region Listbox-Checkbox

    private ArrayList<CulturalInterestType> getCountryData() {

        //Array list of countries
        ArrayList<CulturalInterestType> countryList = new ArrayList<>();

        CulturalInterestType country = new CulturalInterestType("", "Cultural place", false);
        countryList.add(country);
        country = new CulturalInterestType("", "Cultural person", false);
        countryList.add(country);
        country = new CulturalInterestType("", "Cultural event", false);
        countryList.add(country);
        country = new CulturalInterestType("", "Folklore", false);
        countryList.add(country);
        country = new CulturalInterestType("", "Physical object", false);
        countryList.add(country);

        return countryList;
    }

    private void displayListView() {

        ArrayList<CulturalInterestType> countryList = getCountryData();

        // create an ArrayAdapter from the String Array
        mDataAdapter = new MyCustomAdapter(this, R.layout.culturalinterest_info, countryList);

        ListView listView = (ListView) findViewById(R.id.mLstViewCITyp);

        // Assign adapter to ListView
        listView.setAdapter(mDataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                CulturalInterestType CulturalInterestType = (CulturalInterestType) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + CulturalInterestType.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<CulturalInterestType> {

        private ArrayList<CulturalInterestType> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<CulturalInterestType> countryList) {

            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<CulturalInterestType>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.culturalinterest_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        CulturalInterestType lCulturalInterestType = (CulturalInterestType) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        lCulturalInterestType.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CulturalInterestType llCulturalInterestType = countryList.get(position);
            holder.code.setText(" (" + llCulturalInterestType.getCode() + ")");
            holder.name.setText(llCulturalInterestType.getName());
            holder.name.setChecked(llCulturalInterestType.isSelected());
            holder.name.setTag(llCulturalInterestType);

            return convertView;
        }

    }

    /*private void checkButtonClick() {

        Button myButton = (Button) findViewById(R.id.findSelected);

        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Country> countryList = mDataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    Country country = countryList.get(i);
                    if(country.isSelected()){
                        responseText.append("\n" + country.getName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });

    }*/

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
        Spinner lCboCulturalInterestType = (Spinner) findViewById(R.id.mDcboCulturalInterestType);

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
        String lCIType = lCboCulturalInterestType.getSelectedItem().toString();

        lPrefs.setValue(
                BaseConstants.Attr_Radar_Switch,
                lMode);

        lPrefs.setValue(
                BaseConstants.Attr_ClientServer_Communication,
                lClientServerCommunication);

        lPrefs.setValue(
                BaseConstants.Attr_CulturalInterest_type,
                lCIType);
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
}
