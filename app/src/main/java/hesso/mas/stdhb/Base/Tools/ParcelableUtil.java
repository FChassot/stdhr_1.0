package hesso.mas.stdhb.Base.Tools;

/**
 * Created by chf on 04.10.2016.
 */

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 01.11.2016
 *
 *
 */
public final class ParcelableUtil {

    // Constructor
    private ParcelableUtil() {
    }

    /**
     *
     * @param aBundle
     * @param aList
     * @param aKey
     */
    public static void putSerializableList(
        Bundle aBundle,
        ArrayList<? extends Serializable> aList, String aKey) {

        if (aList == null) {
            return;
        }

        aBundle.putInt(aKey + "size", aList.size());

        for (int i = 0; i < aList.size(); i++) {
            aBundle.putSerializable(aKey + i, aList.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getSerializableArrayList(
        Bundle aBundle, String key) {

        int lSize = aBundle.getInt(key + "size", -1);

        if (lSize < 0) {
            return null;
        }

        ArrayList<T> result = new ArrayList<>();

        for (int i = 0; i < lSize; i++) {
            result.add((T) aBundle.getSerializable(key + i));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static void putParcelableList(
        Bundle aBundle, List<? extends Parcelable> aList, String aKey) {

        if (aList == null) {
            return;
        }

        ArrayList<? extends Parcelable> arrayList;

        if (aList instanceof ArrayList) {
            arrayList = (ArrayList<? extends Parcelable>) aList;
        } else {
            arrayList = new ArrayList<>(aList);
        }

        aBundle.putParcelableArrayList(aKey, arrayList);
    }
}
