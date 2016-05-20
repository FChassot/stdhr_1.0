package hesso.mas.stdhb.Gui;

import hesso.mas.stdhb.Common.myString;
import hesso.mas.stdhb.DataAccess.CitizenEndPoint;
import hesso.mas.stdhb.QueryEngine.*;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

import hesso.mas.stdhbtests.R;

/**
 * Created by Frédéric Chassot on 11.05.2016.
 * This is the Activity in which the user will be able to request the
 * sematic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.btnRSearch);

        // Positionner un listener sur ce bouton
        mBtnSearch.setOnClickListener(this);
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){
        if (view.getId()==R.id.btnRSearch) { // C'est notre bouton ? oui, alors affichage d'un message
            /*HttpBinding request = new HttpBinding();
            CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint();

            lCitizenEndPoint.Service("http://dbpedia.org/sparql");

            //Toast.makeText(this,"Requête HTTP!", Toast.LENGTH_SHORT).show();
            String lResponse = request.DoHttpBinding(lCitizenEndPoint);*/

            OkHttpRClient lClient = new OkHttpRClient();
            String getResponse = myString.Empty();

            try {
                getResponse = lClient.doGetRequest("http://www.vogella.com");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, getResponse, Toast.LENGTH_SHORT).show();
        }
    }
}
