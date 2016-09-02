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

    int mX = 100;
    int mY = 100;
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
    RadarMarker(int aX, int aY, int aColor) {
        mX = aX;
        mY = aY;
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
    public int getX() { return mX; }

    // Getter
    public int getY() { return mY; }

    // Getter
    public int getColor() { return mColor; }

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

