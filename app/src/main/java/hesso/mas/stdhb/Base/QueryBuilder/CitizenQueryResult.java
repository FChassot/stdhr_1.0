package hesso.mas.stdhb.Base.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 18.09.2016.
 */
public class CitizenQueryResult {

    private List<CitizenDbObject> mCitizenDbObjects = new ArrayList<CitizenDbObject>();

    // Constructor
    public CitizenQueryResult() {}

    /**
     * Add an CitizenDbObject in the CitizenQueryResult's list
     *
     * @param aCitizenDbObject
     */
    public void Add(CitizenDbObject aCitizenDbObject) {
        mCitizenDbObjects.add(aCitizenDbObject);
    }

    /**
     * Add
     *
     * @param aCitizenDbObjects
     */
    public void AddRange(List<CitizenDbObject> aCitizenDbObjects) {
        mCitizenDbObjects = aCitizenDbObjects;
    }

    /**
     * Returns the size of the CitizenQueryResult's list
     * @return
     */
    public int Count() {
        return mCitizenDbObjects.size();
    }

    public List<CitizenDbObject> Iter() { return mCitizenDbObjects; }
}
