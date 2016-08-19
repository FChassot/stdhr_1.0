package hesso.mas.stdhb.Gui;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;

import hesso.mas.stdhb.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Gui.Radar.RadarActivity;

import hesso.mas.stdhb.Gui.CitizenSearch.SearchActivity;
import hesso.mas.stdhbtests.R;

/**
 * Created by Frédéric Chassot (chf) on 11.06.2016.
 *
 * This is the main Activity of the Stdhr application
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération de l'instance bouton préférences
        Button mPreferencesButton = (Button)findViewById(R.id.btnpreferences);
        Button mRadarButton = (Button)findViewById(R.id.btnradar);
        Button mSearchButton = (Button)findViewById(R.id.btnsearch);
        Button mMapButton = (Button)findViewById(R.id.btnMap);

        // Positionner un listener sur ce bouton
        assert mPreferencesButton != null;
        mPreferencesButton.setOnClickListener(this);

        assert mRadarButton != null;
        mRadarButton.setOnClickListener(this);

        assert mSearchButton != null;
        mSearchButton.setOnClickListener(this);

        assert mMapButton != null;
        mMapButton.setOnClickListener(this);

        Preferences lPrefs = new Preferences(this);

        lPrefs.setValue(BaseConstants.Attr_Rayon_Radar, 500);

        // Affiche les coordonnées GPS actuel de l'appareil
        Location lActualLocation = lPrefs.getValue("location");

        StringBuilder builder = new StringBuilder();

        if (lActualLocation != null) {
            builder.append("\n Coordonnées GPS: " + lActualLocation.toString());

            TextView rayonSettingsView = (TextView) findViewById(R.id.textViewRayon);

            assert rayonSettingsView != null;
            rayonSettingsView.setText(builder.toString());
        }
    }

    /**
     * Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
     *
     */
    public void onClick(View view){
        if (view.getId()==R.id.btnpreferences){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnradar){
            Intent intent = new Intent(MainActivity.this, RadarActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnsearch){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnMap){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
}

