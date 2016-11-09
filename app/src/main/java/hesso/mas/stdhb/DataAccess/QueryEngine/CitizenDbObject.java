package hesso.mas.stdhb.DataAccess.QueryEngine;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chf on 21.09.2016.
 *
 * Represents an object retrieved from the Citizen Endpoint
 */
public class CitizenDbObject implements Parcelable {

    // Member variable
    private Map<String, String> mMap = new HashMap<String, String>();

    // Constructor
    public CitizenDbObject() {
    }

    // Constructor
    private CitizenDbObject(Parcel in) {
        in.readMap(mMap, getClass().getClassLoader());
    }

    public void put(String aVariable, String aValue) {
        mMap.put(aVariable, aValue);
    }

    /**
     * This method checks if the variable is contained in the response
     *
     * @param aVariable
     *
     * @return return true if the variable is contained in the citizen Db Object
     */
    public Boolean containsVariable(String aVariable) {

        if (mMap.containsKey(aVariable)) {
            return true;
        }

        return false;
    }

    /**
     * Get the value of the sparql variable
     *
     * @param aVariable
     *
     * @return
     */
    public String GetValue(String aVariable) {
        return mMap.get(aVariable);
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param aDest The Parcel in which the object should be written.
     * @param aFlags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    public void writeToParcel(Parcel aDest, int aFlags) {
        aDest.writeMap(mMap);
    }

    /**
     * Classes implementing the Parcelable interface must also have a non-null
     * static field called CREATOR of a type that implements the Parcelable.Creator
     * interface.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CitizenDbObject createFromParcel(Parcel in) {
            return new CitizenDbObject(in);
        }

        public CitizenDbObject[] newArray(int size) {
            return new CitizenDbObject[size];
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
}
