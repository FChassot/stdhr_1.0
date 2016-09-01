package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;

/**
 * Created by chf on 21.07.2016.
 *
 * This class reprensents a marker which is displayed on the radar view.
 */
public class RadarMarker extends Paint {

    Location mLocation;

    // Constructor
    public void RadarMarker(Location aLocation, Color aColor) {
        mLocation = aLocation;
    };

    public void RadarMarker(
        int aColor,
        boolean aAntiAlias,
        Paint.Style aStyle,
        float aStrokeWidth,
        int aAlpha) {

        //mLocation = aLocation;
        super.setColor(aColor);
        super.setAntiAlias(aAntiAlias);
        super.setStyle(aStyle);
        super.setStrokeWidth(aStrokeWidth);
        super.setAlpha(aAlpha);

    };

    public void setLocation(Location aLocation) {
        mLocation = aLocation;

    }

    public void setLongitude(Double aLongitude) {
        mLocation.setLongitude(aLongitude);
    }

    public void setLatitude(Double aLatitude) {
        mLocation.setLatitude(aLatitude);
    }

    public Location getLocation() {
        return mLocation;
    }

}

