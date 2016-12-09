package hesso.mas.stdhb.Client.Gui.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;

import hesso.mas.stdhb.Client.Gui.Citizen.CityZenActivity;
import hesso.mas.stdhb.Client.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarActivity;

import hesso.mas.stdhb.Client.Gui.Search.SearchActivity;
import hesso.mas.stdhbtests.R;

import javax.inject.Inject;

/**
 * Created by chf on 11.06.2016.
 *
 * This class represents the main Activity of the application
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    // Member variable
     @Inject Preferences mPrefs;

    /*@Inject
    CustomApplication customApplication;*/

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
        super.onCreate(savedInstanceState);  // Always call the superclass method first

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_main);

        mPrefs = new Preferences(this);

        // Assign singleton instances to fields
        //Injector.INSTANCE.getApplicationComponent().inject(this);

        // Finds the views that was identified by an id attribute
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
     * Called when the activity is becoming visible to the user.
     * Followed by onResume() if the activity comes to the foreground,
     * or onStop() if it becomes hidden.
     */
    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first

        Button lRadarButton = (Button)findViewById(R.id.mBtnradar);

        boolean lRadarMode =
                mPrefs.getMyBooleanPref(
                    this,
                    BaseConstants.Attr_Radar_Switch,
                    false);

        lRadarButton.setEnabled(lRadarMode);
    }

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume()
     * method.
     *
     * Be aware that the system calls this method every time your activity comes into the foreground,
     * including when it's created for the first time. As such, you should implement onResume() to
     * initialize components that you release during onPause() and perform any other initializations
     * that must occur each time the activity enters the Resumed state (such as begin animations and
     * initialize components only used while the activity has user focus).
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Button lRadarButton = (Button)findViewById(R.id.mBtnradar);

        boolean lRadarMode =
                mPrefs.getMyBooleanPref(
                        this,
                        BaseConstants.Attr_Radar_Switch,
                        false);

        lRadarButton.setEnabled(lRadarMode);
    }

    /**
     * Called when the system is about to start resuming a previous activity. This is typically used
     * to commit unsaved changes to persistent data, stop animations and other things that may be
     * consuming CPU, etc. Implementations of this method must be very quick because the next activity
     * will not be resumed until this method returns.
     * Followed by either onResume() if the activity returns back to the front, or onStop() if it
     * becomes invisible to the user.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * The onClick() method is called when a button is actually clicked.
     * This method is called by the OnClickListener.
     */
    public void onClick(View aView){

        if (aView.getId()==R.id.mImgSettings){
            Intent lIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(lIntent);
        }
        if (aView.getId()==R.id.mBtnradar){
            Intent lIntent = new Intent(MainActivity.this, RadarActivity.class);
            startActivity(lIntent);
        }
        if (aView.getId()==R.id.mBtnSearch){
            Intent lIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(lIntent);
        }
        if (aView.getId()==R.id.mBtnMap){
            Intent lIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(lIntent);
        }
    }
}

