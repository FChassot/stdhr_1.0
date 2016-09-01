package hesso.mas.stdhb.Gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

import hesso.mas.stdhb.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Gui.Radar.RadarActivity;

import hesso.mas.stdhb.Gui.Citizen.SearchActivity;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.06.2016.
 *
 * This class represent the main Activity of the Stdhr application
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération de l'instance bouton préférences
        Button mPreferencesButton = (Button)findViewById(R.id.mDBtnpreferences);
        Button mRadarButton = (Button)findViewById(R.id.mBtnradar);
        Button mSearchButton = (Button)findViewById(R.id.mBtnSearch);
        Button mMapButton = (Button)findViewById(R.id.mBtnMap);
        ImageView mImgViewSettings = (ImageView)findViewById(R.id.mImgViewSettings);

        // Positionner un listener sur ce bouton
        assert mPreferencesButton != null;
        mPreferencesButton.setOnClickListener(this);

        assert mRadarButton != null;
        mRadarButton.setOnClickListener(this);

        assert mSearchButton != null;
        mSearchButton.setOnClickListener(this);

        assert mMapButton != null;
        mMapButton.setOnClickListener(this);

        /*Preferences lPrefs = new Preferences(this);

        // Affiche les coordonnées GPS actuel de l'appareil
        Location lActualLocation = lPrefs.getValue("location");

        StringBuilder builder = new StringBuilder();

        if (lActualLocation != null) {
            builder.append("\n Coordonnées GPS: " + lActualLocation.toString());

            TextView rayonSettingsView = (TextView) findViewById(R.id.textViewRayon);

            assert rayonSettingsView != null;
            rayonSettingsView.setText(builder.toString());
        }*/
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     */
    public void onClick(View view){
        if (view.getId()==R.id.mDBtnpreferences){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.mBtnradar){
            Intent intent = new Intent(MainActivity.this, RadarActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.mBtnSearch){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.mBtnMap){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.mImgViewSettings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    }
}

