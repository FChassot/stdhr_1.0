package hesso.mas.stdhb.Base.QueryBuilder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chf on 21.09.2016.
 */
public class CitizenDbObject implements Parcelable {

    //private Literal mLiteral;
    //private URI mUri;

    private String mValue;

    /*public CitizenDbObject(Literal aObject) {
        if (aObject instanceof  Literal) { mLiteral = aObject; }
    }

    public CitizenDbObject(URI aObject) {
        if (aObject instanceof  URI) { mUri = aObject; }
    }*/

    public CitizenDbObject(String aObject) {
        if (aObject instanceof String) {
            mValue = aObject;
        }
    }

    // Constructor
    private CitizenDbObject(Parcel in) {
        in.readString();
    }


    public void writeToParcel(Parcel aDest, int flags) {
        aDest.writeString(mValue);
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
