package hesso.mas.stdhb.Gui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import hesso.mas.stdhbtests.R;

/**
 * Created by Frédéric Chassot on 11.05.2016.
 * This is the main Activity of this application
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
        mPreferencesButton.setOnClickListener(this);
        mRadarButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mMapButton.setOnClickListener(this);

        // Afficher le rayon actuellement configuré
        showUserSettings();
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){
        if (view.getId()==R.id.btnpreferences){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnradar){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnsearch){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnMap){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, GMapActivity.class);
            startActivity(intent);
        }
    }

    /**
     *
     */
    private void showUserSettings() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append("\n Rayon: " + sharedPrefs.getString("prefRadarRayon", "NULL"));

        TextView rayonSettingsView = (TextView) findViewById(R.id.textViewRayon);

        rayonSettingsView.setText(builder.toString());
    }

}

