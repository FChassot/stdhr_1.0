package hesso.mas.stdhb.DataAccess.QueryEngine.Response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 18.09.2016.
 *
 * This class represents the result of a sparql query. This class implements
 * the class Parcelable.
 */
public class CityZenQueryResult implements Parcelable {

    // Member variable
    private List<CityZenDbObject> mList = new ArrayList<>();

    // Constructor
    public CityZenQueryResult() {}

    // Constructor
    private CityZenQueryResult(Parcel in) {
        in.readList(mList, getClass().getClassLoader());
    }

    /**
     * Add an CityZenDbObject in the CityZenQueryResult's list
     *
     * @param cityZenDbObject The object to add in the list
     */
    public void Add(CityZenDbObject cityZenDbObject) {
        mList.add(cityZenDbObject);
    }

    /**
     * Add a list of objects in the list
     *
     * @param cityZenDbObjects The list of objects to add in the list
     */
    public void AddRange(List<CityZenDbObject> cityZenDbObjects) {
        mList = cityZenDbObjects;
    }

    /**
     * Returns the size of the CityZenQueryResult's list
     *
     */
    public int Count() {
        return mList.size();
    }

    /**
     * Exposes the list of CityZenDbObject
     *
     * @return The list of CityZenDbObject
     */
    public List<CityZenDbObject> Results() { return mList; }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mList);
    }

    /**
     * Classes implementing the Parcelable interface must also have a non-null
     * static field called CREATOR of a type that implements the Parcelable.Creator
     * interface.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CityZenQueryResult createFromParcel(Parcel in) {
            return new CityZenQueryResult(in);
        }

        public CityZenQueryResult[] newArray(int size) {
            return new CityZenQueryResult[size];
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
