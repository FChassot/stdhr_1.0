package hesso.mas.stdhb.Base.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 30.09.2016.
 *
 * Implement a collection von String.
 */
public class ValidationDescCollection {

    // member variable
    private List<String> mList = new ArrayList<>();

    /**
     * Method to access the list
     *
     * @return the list of strings
     */
    public List<String> values() {
        return mList;
    }

    /**
     * Add a new String in the list
     *
     * @param aValDesc String to add
     */
    public void add(String aValDesc) {
        mList.add(aValDesc);
    }

    /**
     * Fill a string in the list, if it still not exist.
     *
     * @param aValDesc String to add
     */
    public void addUnique(String aValDesc) {
        if (!mList.contains(aValDesc)) {
            add(aValDesc);
        }
    }

    /**
     * Give the size of the list
     *
     */
    public int count() { return mList.size(); }

    /**
     * Return true if the list at least one entry contains
     *
     */
    public boolean any() { return (mList.size() > 0); }
}
