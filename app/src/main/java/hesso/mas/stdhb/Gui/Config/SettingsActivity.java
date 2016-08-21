package hesso.mas.stdhb.Gui.Config;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Gui.MainActivity;

import hesso.mas.stdhbtests.R;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        Button mBtnSave = (Button)findViewById(R.id.btnSave);

        EditText mRayon = (EditText)findViewById(R.id.mDTxtRayon);
        Preferences lPrefs = new Preferences(this);

        Integer lRaySearch = lPrefs.getPrefValue(BaseConstants.Attr_Ray_Search, -1);
        Boolean lRadarMode = lPrefs.getBooleanPrefValue(BaseConstants.Attr_Radar_Switch, false);

        if (lRaySearch == -1) {
            lPrefs.setValue(BaseConstants.Attr_Ray_Search, 500);
            mRayon.setText("500");
        } else {
            mRayon.setText(lRaySearch.toString());
        }

        Switch lRadarSwitch = (Switch)findViewById(R.id.RadarSwitch);

        if (lRadarMode == true) {
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
            EditText lRayonRadar = (EditText)findViewById(R.id.mDTxtRayon);

            Preferences lPrefs = new Preferences(this);
            Integer lRayon = Integer.parseInt(lRayonRadar.getText().toString());
            lPrefs.setValue(BaseConstants.Attr_Ray_Search, lRayon);

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
