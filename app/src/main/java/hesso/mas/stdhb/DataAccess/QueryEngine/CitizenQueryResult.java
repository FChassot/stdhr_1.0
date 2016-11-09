package hesso.mas.stdhb.DataAccess.QueryEngine;

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
public class CitizenQueryResult implements Parcelable {

    public List<CitizenDbObject> mList = new ArrayList<>();

    // Constructor
    public CitizenQueryResult() {}

    // Constructor
    private CitizenQueryResult(Parcel in) {
        in.readList(mList, getClass().getClassLoader());
    }

    /**
     * Add an CitizenDbObject in the CitizenQueryResult's list
     *
     * @param aCitizenDbObject
     */
    public void Add(CitizenDbObject aCitizenDbObject) {
        mList.add(aCitizenDbObject);
    }

    /**
     * Add
     *
     * @param aCitizenDbObjects
     */
    public void AddRange(List<CitizenDbObject> aCitizenDbObjects) {
        mList = aCitizenDbObjects;
    }

    /**
     * Returns the size of the CitizenQueryResult's list
     *
     * @return
     */
    public int Count() {
        return mList.size();
    }

    /**
     * Exposes the list of CitizenDbObject
     *
     * @return
     */
    public List<CitizenDbObject> Results() { return mList; }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param aDest The Parcel in which the object should be written.
     * @param aFlags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    public void writeToParcel(Parcel aDest, int aFlags) {
        aDest.writeList(mList);
    }

    /**
     * Classes implementing the Parcelable interface must also have a non-null
     * static field called CREATOR of a type that implements the Parcelable.Creator
     * interface.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CitizenQueryResult createFromParcel(Parcel in) {
            return new CitizenQueryResult(in);
        }

        public CitizenQueryResult[] newArray(int size) {
            return new CitizenQueryResult[size];
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
