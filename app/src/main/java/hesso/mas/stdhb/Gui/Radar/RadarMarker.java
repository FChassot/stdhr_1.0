package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.graphics.Paint;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 21.07.2016.
 *
 * This class represents a marker which is displayed on the radar view.
 */
public class RadarMarker extends Paint implements Parcelable {

    Location mLocation = new Location(MyString.EMPTY_STRING);

    int mXViewPosition = 0;

    int mYViewPosition = 0;

    /**
     * The color of the marker
     */
    int mColor;

    /**
     * The text which will be displayed by selecting the cultural object
     */
    String mTitle = MyString.EMPTY_STRING;

    //region Constructors

    // Constructor
    RadarMarker() {
        super.setColor(Color.BLUE);
        super.setAntiAlias(true);
        super.setStyle(Paint.Style.FILL);
        super.setStrokeWidth(5.0F);
        super.setAlpha(0);
    }

    // Constructor
    RadarMarker(
        int aPositionX,
        int aPositionY,
        double aLatitude,
        double aLongitude,
        int aColor) {

        mXViewPosition = aPositionX;
        mYViewPosition = aPositionY;
        mLocation.setLatitude(aLatitude);
        mLocation.setLongitude(aLongitude);
        mColor = aColor;
    }

    // Constructor
    RadarMarker(
            int aPositionX,
            int aPositionY,
            double aLatitude,
            double aLongitude,
            int aColor,
            String aTitle) {

        mXViewPosition = aPositionX;
        mYViewPosition = aPositionY;
        mLocation.setLatitude(aLatitude);
        mLocation.setLongitude(aLongitude);
        mColor = aColor;
        mTitle = aTitle;
    }

    // Constructor
    RadarMarker(
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

    }

    // Constructor
    private RadarMarker(Parcel in) {

        mLocation = in.readParcelable(Location.class.getClassLoader());
        mXViewPosition = in.readInt();
        mYViewPosition = in.readInt();
        mColor = in.readInt();
        mTitle = in.readString();

    }

    //endregion

    // Getter
    public int getPositionX() { return mXViewPosition; }

    // Getter
    public int getPositionY() { return mYViewPosition; }

    // Getter
    public int getColor() { return mColor; }

    // Setter
    public void setTitle(String aTitle) {
        mTitle = aTitle;
    }

    // Getter
    public String getTitle() {
        return mTitle;
    }

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

    //region Parcelable

    public void writeToParcel(Parcel aDest, int flags) {

        aDest.writeParcelable(mLocation, flags);
        aDest.writeInt(mXViewPosition);
        aDest.writeInt(mYViewPosition);
        aDest.writeInt(mColor);
        aDest.writeString(mTitle);
    }

    /**
     * Classes implementing the Parcelable interface must also have a non-null
     * static field called CREATOR of a type that implements the Parcelable.Creator
     * interface.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RadarMarker createFromParcel(Parcel in) {
            return new RadarMarker(in);
        }

        public RadarMarker[] newArray(int size) {
            return new RadarMarker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //endregion

}

