package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.graphics.Paint;

import android.location.Location;

/**
 * Created by chf on 21.07.2016.
 *
 * This class represents a marker which is displayed on the radar view.
 */
public class RadarMarker extends Paint {

    Location mLocation;

    int mPositionX = 100;

    int mPositionY = 100;

    int mColor;

    // Constructor
    RadarMarker() {
        super.setColor(Color.BLUE);
        super.setAntiAlias(true);
        super.setStyle(Paint.Style.FILL);
        super.setStrokeWidth(5.0F);
        super.setAlpha(0);
    };

    // Constructor
    RadarMarker(int aPositionX, int aPositionY, int aColor) {
        mPositionX = aPositionX;
        mPositionY = aPositionY;
        mColor = aColor;
    };

    // Constructor
    void RadarMarker(
        int aColor,
        boolean aAntiAlias,
        Paint.Style aStyle,
        float aStrokeWidth,
        int aAlpha) {

        super.setColor(aColor);
        super.setAntiAlias(aAntiAlias);
        super.setStyle(aStyle);
        super.setStrokeWidth(aStrokeWidth);
        super.setAlpha(aAlpha);

    };

    // Getter
    public int getPositionX() { return mPositionX; }

    // Getter
    public int getPositionY() { return mPositionY; }

    // Getter
    public int getColor() { return mColor; }

    // Setter
    public void setLocation(Location aLocation) {
        mLocation = aLocation;
    }

    // Getter
    public Location getLocation() {
        return mLocation;
    }

    // Setter
    public void setLongitude(Double aLongitude) {
        mLocation.setLongitude(aLongitude);
    }

    // Getter
    public double getLongitude() { return mLocation.getLongitude(); }

    // Setter
    public void setLatitude(Double aLatitude) { mLocation.setLatitude(aLatitude); }

    // Getter
    public double getLatitude() { return mLocation.getLatitude(); }

    // Getter
    public double getAltitude() { return mLocation.getAltitude(); }

}

