package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

        mRadarView = (RadarView) findViewById(R.id.radarView);

        RadarMarker lMarker1 = new RadarMarker(0, 0, Color.BLUE);
        RadarMarker lMarker2 = new RadarMarker(120, 150, Color.RED);
        RadarMarker lMarker3 = new RadarMarker(150, 201, Color.RED);

        RadarMarker lMarkers[] = new RadarMarker[3];

        lMarkers[0] = lMarker3;
        lMarkers[1] = lMarker2;
        lMarkers[2] = lMarker1;

        this.startAnimation(mRadarView);
        this.updateMarkers(mRadarView, lMarkers);
        
    }

    public void stopAnimation(View view) {
        if (mRadarView != null) mRadarView.stopAnimation();
    }

    public void updateMarkers(View view, RadarMarker[] lMarkers) {
        if (mRadarView != null) mRadarView.updateMarkers(lMarkers);
    }

    public void startAnimation(View view) {
        if (mRadarView != null) mRadarView.startAnimation();
    }

}
