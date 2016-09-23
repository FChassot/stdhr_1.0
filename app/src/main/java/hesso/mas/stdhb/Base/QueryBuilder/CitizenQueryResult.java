package hesso.mas.stdhb.Base.QueryBuilder;

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

    private List<CitizenDbObject> mList = new ArrayList<>();

    // Constructor
    public CitizenQueryResult() {}

    // Constructor
    private CitizenQueryResult(Parcel in) {
        in.readList(mList, null);
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
    public List<CitizenDbObject> Iter() { return mList; }

    public void writeToParcel(Parcel aDest, int flags) {
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

    @Override
    public int describeContents() {
        return 0;
    }

}
