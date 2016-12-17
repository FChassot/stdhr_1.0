package hesso.mas.stdhb.DataAccess.QueryEngine.Response;

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
    private Map<String, String> mMap = new HashMap<>();

    // Constructor
    public CitizenDbObject() {
    }

    // Constructor
    private CitizenDbObject(Parcel in) {
        in.readMap(mMap, getClass().getClassLoader());
    }

    public void put(String variable, String value) {
        mMap.put(variable, value);
    }

    /**
     * This method checks if the variable is contained in the response
     *
     * @param aVariable The variable
     *
     * @return return true if the variable is contained in the citizen Db Object
     */
    public Boolean containsVariable(String aVariable) {

        return mMap.containsKey(aVariable);
    }

    /**
     * Get the value of the sparql variable
     *
     * @param variable
     */
    public String GetValue(String variable) {
        return mMap.get(variable);
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mMap);
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
     */
    @Override
    public int describeContents() {
        return 0;
    }
}
