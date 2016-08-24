package hesso.mas.stdhb.Gui.Config;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Gui.MainActivity;

import hesso.mas.stdhbtests.R;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    private String array_spinner[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        // Here come all the options that you wish to show depending on the
        // size of the array.
        array_spinner=new String[3];
        array_spinner[0]="Soap";
        array_spinner[1]="Rest";
        array_spinner[2]="Rdf4j";

        Spinner lSpinner = (Spinner) findViewById(R.id.Spinner01);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        lSpinner.setAdapter(adapter);

        Button mBtnSave = (Button)findViewById(R.id.btnSave);

        EditText mRayon = (EditText)findViewById(R.id.mDTxtRayon);
        Preferences lPrefs = new Preferences(this);

        Integer lRaySearch = lPrefs.getPrefValue(BaseConstants.Attr_Ray_Search, -1);

        if (lRaySearch == -1) {
            lPrefs.setValue(BaseConstants.Attr_Ray_Search, 500);
            mRayon.setText(BaseConstants.Attr_Default_Ray_Search);
        } else {
            mRayon.setText(lRaySearch.toString());
        }

        Integer lRadarMode = lPrefs.getPrefValue(BaseConstants.Attr_Radar_Switch, 0);
        Switch lRadarSwitch = (Switch)findViewById(R.id.RadarSwitch);

        if (lRadarMode == 1) {
            lRadarSwitch.setChecked(true);
        }
        else {
            lRadarSwitch.setChecked(false);
        }

        // Positionner un listener sur ce bouton
        mBtnSave.setOnClickListener((OnClickListener) this);
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     *
     */
    public void onClick(View view){

        if (view.getId()==R.id.btnSave){
            EditText lRaySearch = (EditText)findViewById(R.id.mDTxtRayon);

            Preferences lPrefs = new Preferences(this);
            Integer lRay = Integer.parseInt(lRaySearch.getText().toString());
            lPrefs.setValue(BaseConstants.Attr_Ray_Search, lRay);

            Switch lRadarMode = (Switch)findViewById(R.id.RadarSwitch);
            Boolean lMode = lRadarMode.isChecked();

            Integer lIntMode = 0;
            if (lMode == true) {lIntMode = 1;}

            lPrefs.setValue(BaseConstants.Attr_Radar_Switch, lIntMode);

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * ...
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        EditText lRaySearch = (EditText)findViewById(R.id.mDTxtRayon);

        Preferences lPrefs = new Preferences(this);
        Integer lRayon = Integer.parseInt(lRaySearch.getText().toString());
        lPrefs.setValue(BaseConstants.Attr_Ray_Search, lRayon);
    }

    /*lSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            Log.d(arrayOfObjects[position]._id);
        }

    });*/
}
