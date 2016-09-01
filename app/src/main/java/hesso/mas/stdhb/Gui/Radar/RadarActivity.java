package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hesso.mas.stdhb.Gui.Radar.*;

import hesso.mas.stdhbtests.R;

public class RadarActivity extends AppCompatActivity {

    RadarView mRadarView = null;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mRadarView = (RadarView) findViewById(R.id.radarView);

        RadarMarker lMarker1 = new RadarMarker(100, 100, Color.RED);
        RadarMarker lMarker2 = new RadarMarker(200, 200, Color.BLUE);

        RadarMarker lMarkers[] = new RadarMarker[2];

        lMarkers[0] = lMarker1;
        lMarkers[1] = lMarker2;

        mRadarView.updateMarkers(lMarkers);
        mRadarView.startAnimation();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Mon test", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    public void stopAnimation(View view) {
        if (mRadarView != null) mRadarView.stopAnimation();
    }

    public void startAnimation(View view) {
        if (mRadarView != null) mRadarView.startAnimation();
    }

}
