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
 * This class represents the main Activity of the application
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Called when the activity is first created. This is where you should do all of your
     * normal static set up: create views, bind data to lists, etc. This method also provides
     * you with a Bundle containing the activity's previously frozen state, if there was one.
     * Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mRadarButton = (Button)findViewById(R.id.mBtnradar);
        Button mSearchButton = (Button)findViewById(R.id.mBtnSearch);
        Button mMapButton = (Button)findViewById(R.id.mBtnMap);
        ImageView mImgSettings = (ImageView)findViewById(R.id.mImgSettings);

        assert mRadarButton != null;
        mRadarButton.setOnClickListener(this);

        assert mSearchButton != null;
        mSearchButton.setOnClickListener(this);

        assert mMapButton != null;
        mMapButton.setOnClickListener(this);

        assert mImgSettings != null;
        mImgSettings.setOnClickListener(this);
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     */
    public void onClick(View view){
        if (view.getId()==R.id.mImgSettings){
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
    }
}

