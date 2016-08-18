package hesso.mas.stdhb.Gui.Config;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        String lRayonRadar = lPrefs.getPrefValue(BaseConstants.Attr_Rayon_Radar, "500");

        if (lRayonRadar == MyString.EMPTY_STRING) {
            lPrefs.setValue(BaseConstants.Attr_Rayon_Radar, 500);
            mRayon.setText("500");
        } else {
            mRayon.setText(lRayonRadar);
        }

        // Positionner un listener sur ce bouton
        mBtnSave.setOnClickListener((OnClickListener) this);
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){

        if (view.getId()==R.id.btnSave){
            EditText lRayonRadar = (EditText)findViewById(R.id.mDTxtRayon);

            Preferences lPrefs = new Preferences(this);
            Integer lRayon = Integer.parseInt(lRayonRadar.getText().toString());
            lPrefs.setValue(BaseConstants.Attr_Rayon_Radar, lRayon);

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
