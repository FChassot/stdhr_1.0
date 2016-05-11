package hesso.mas.stdhb.GUI;

import hesso.mas.stdhb.QUERYENGINE.EndPoint;
import hesso.mas.stdhb.QUERYENGINE.HttpBinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hesso.mas.spatio_temporaldigitalheritagebrowsing.R;

public class RechercheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.btnRSearch);

        // Positionner un listener sur ce bouton
        mBtnSearch.setOnClickListener((View.OnClickListener) this);
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){
        /*if (view.getId()==R.id.btnRSearch) { // C'est notre bouton ? oui, alors affichage d'un message
            HttpBinding request = new HttpBinding();
            EndPoint lEndPoint = new EndPoint();

            lEndPoint.Uri("http://www.android.com/");

            Toast.makeText(this,"Requête HTTP!", Toast.LENGTH_SHORT).show();
            //request.DoHttpBinding(lEndPoint.Uri());

        }*/
    }
}
