package hesso.mas.stdhb.Base.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 30.09.2016.
 *
 * Implement a collection von String.
 */
public class ValidationDescCollection {

    // Member variable
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
     * @param valDesc String to add
     */
    public void add(String valDesc) {
        mList.add(valDesc);
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
     * @return
     */
    public int count() { return mList.size(); }

    /**
     * Return true if the list at least one entry contains
     *
     */
    public boolean any() {
        if (mList != null) {
            return (mList.size() > 0);
        }

        return false;
    }
}
