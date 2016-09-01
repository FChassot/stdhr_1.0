package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.location.Location;

/**
 * Created by chf on 21.07.2016.
 *
 * This class reprensents a marker which is displayed on the radar view.
 */
public class RadarMarker {

    Location mLocation;

    Color mColor;

    // Constructor
    public void RadarMarker(Location aLocation, Color aColor) {
        mLocation = aLocation;
        mColor = aColor;
    };

    // Setter
    public void setColor(Color aColor) {mColor = aColor;}
    public Color getColor() {return mColor;}

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

