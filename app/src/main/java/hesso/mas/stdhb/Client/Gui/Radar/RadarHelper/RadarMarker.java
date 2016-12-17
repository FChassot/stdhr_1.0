package hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

import android.graphics.Color;
import android.graphics.Paint;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 21.07.2016.
 *
 * This class represents a marker which is displayed on the radar view.
 */
public class RadarMarker extends Paint implements Parcelable {

    // Member variables
    private Location mLocation = new Location(MyString.EMPTY_STRING);

    private int mXViewPosition = 0;

    private int mYViewPosition = 0;

    private int mXViewCurrentPosition = 0;

    private int mYViewCurrentPosition = 0;

    /**
     * The color of the marker
     */
    private int mColor;

    /**
     * The text which will be displayed by selecting the cultural object
     */
    private String mTitle = MyString.EMPTY_STRING;

    /**
     *
     */
    private String mObjectId = MyString.EMPTY_STRING;

    /**
     *
     */
    private String mDescription = MyString.EMPTY_STRING;

    //region Constructors

    // Constructor
    public RadarMarker() {
        super.setColor(Color.BLUE);
        super.setAntiAlias(true);
        super.setStyle(Paint.Style.FILL);
        super.setStrokeWidth(5.0F);
        super.setAlpha(0);

        mTitle = MyString.EMPTY_STRING;
        mObjectId = MyString.EMPTY_STRING;
        mDescription = MyString.EMPTY_STRING;
    }

    // Constructor
    public RadarMarker(
        int positionX,
        int positionY,
        double latitude,
        double longitude,
        int color,
        String title,
        String objectId,
        String description) {

        super();

        mXViewPosition = positionX;
        mYViewPosition = positionY;
        mLocation.setLatitude(latitude);
        mLocation.setLongitude(longitude);
        mColor = color;
        mTitle = title;
        mObjectId = objectId;
        mDescription = description;

    }

    // Constructor
    private RadarMarker(Parcel in) {

        mLocation = in.readParcelable(Location.class.getClassLoader());
        mXViewPosition = in.readInt();
        mYViewPosition = in.readInt();
        mXViewCurrentPosition = in.readInt();
        mYViewCurrentPosition = in.readInt();
        mColor = in.readInt();
        mTitle = in.readString();
        mObjectId = in.readString();
        mDescription = in.readString();
    }

    //endregion

    // Setter
    public void setPositionX(int x) { mXViewPosition = x; }

    // Getter
    public int getPositionX() { return mXViewPosition; }

    // Setter
    public void setPositionY(int y) { mYViewPosition = y; }

    // Getter
    public int getPositionY() { return mYViewPosition; }

    // Setter
    public void setCurrentPositionX(int x) { mXViewCurrentPosition = x; }

    // Getter
    public int getCurrentPositionX() { return mXViewCurrentPosition; }

    // Setter
    public void setCurrentPositionY(int y) { mYViewCurrentPosition = y; }

    // Getter
    public int getCurrentPositionY() { return mYViewCurrentPosition; }

    // Getter
    public int getColor() { return mColor; }

    // Setter
    public void setTitle(String title) {
        mTitle = title;
    }

    // Getter
    public String getTitle() {
        return mTitle;
    }

    // Setter
    public void setObjectId(String objectId) { mObjectId = objectId; }

    // Getter
    public String getObjectId() { return mObjectId; }

    // Setter
    public void setDescription(String description) { mDescription = description; }

    // Getter
    public String getDescription() { return mDescription; }

    // Setter
    public void setLocation(Location location) {
        mLocation = location;
    }

    // Getter
    public Location getLocation() {
        return mLocation;
    }

    // Setter
    public void setLongitude(Double longitude) {
        mLocation.setLongitude(longitude);
    }

    // Getter
    public double getLongitude() { return mLocation.getLongitude(); }

    // Setter
    public void setLatitude(Double latitude) { mLocation.setLatitude(latitude); }

    // Getter
    public double getLatitude() { return mLocation.getLatitude(); }

    // Getter
    public double getAltitude() { return mLocation.getAltitude(); }

    //region Parcelable

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(mLocation, flags);
        dest.writeInt(mXViewPosition);
        dest.writeInt(mYViewPosition);
        dest.writeInt(mXViewCurrentPosition);
        dest.writeInt(mYViewCurrentPosition);
        dest.writeInt(mColor);
        dest.writeString(mTitle);
        dest.writeString(mObjectId);
        dest.writeString(mDescription);
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

    /**
     * Indicates that the Parcelable object's flattened representation includes a file descriptor.
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    //endregion

}

