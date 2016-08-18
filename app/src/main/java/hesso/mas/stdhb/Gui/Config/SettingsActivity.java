package hesso.mas.stdhb.Gui.Config;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Gui.MainActivity;
import hesso.mas.stdhbtests.R;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        /*Button mBtnSave = (Button)findViewById(R.id.btnSave);

        EditText mRayon = (EditText)findViewById(R.id.mDTxtRayon);
        Preferences lPrefs = new Preferences(this);

        Location lRayonRadar = lPrefs.getValue(BaseConstants.Attr_Rayon_Radar);

        lPrefs.setValue(BaseConstants.Attr_Rayon_Radar, 50);

        mRayon.setText(lPrefs.getValue(BaseConstants.Attr_Rayon_Radar).toString());

        // Positionner un listener sur ce bouton
        mBtnSave.setOnClickListener((OnClickListener) this);*/
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){

        if (view.getId()==R.id.btnSave){
            EditText mRayonDeRecherche = (EditText)findViewById(R.id.mDTxtRayon);

            Preferences lPrefs = new Preferences(this);
            lPrefs.setValue(BaseConstants.Attr_Rayon_Radar, Integer.parseInt(mRayonDeRecherche.getText().toString()));

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
