package hesso.mas.stdhb.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

import hesso.mas.spatio_temporaldigitalheritagebrowsing.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération de l'instance bouton préférences
        Button mPreferencesButton = (Button)findViewById(R.id.btnpreferences);
        Button mRadarButton = (Button)findViewById(R.id.btnradar);
        Button mSearchButton = (Button)findViewById(R.id.btnsearch);

        // Positionner un listener sur ce bouton
        mPreferencesButton.setOnClickListener((OnClickListener)this);
        mRadarButton.setOnClickListener((OnClickListener)this);
        mSearchButton.setOnClickListener((OnClickListener)this);
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){
        if (view.getId()==R.id.btnpreferences){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnradar){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (view.getId()==R.id.btnsearch){ // C'est notre bouton ? oui, alors affichage d'un message
            Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
            startActivity(intent);
        }
    }

}

