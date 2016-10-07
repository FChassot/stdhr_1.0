package hesso.mas.stdhb.DataAccess.QueryEngine;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chf on 21.09.2016.
 */
public class CitizenDbObject implements Parcelable {

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

    public String GetValue(String aVariable) {
        return mMap.get(aVariable);
    }

    public void writeToParcel(Parcel aDest, int flags) {
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

    @Override
    public int describeContents() {
        return 0;
    }
}
