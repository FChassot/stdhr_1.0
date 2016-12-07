package hesso.mas.stdhb.Base.Tools;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 01.11.2016
 *
 * Utility class for the serialization
 */
public final class ParcelableUtil {

    // private constructor
    private ParcelableUtil() {}

    /**
     * Inserts a Serializable value into the mapping of this Bundle, replacing any existing value
     * for the given key. Either key or value may be null.
     *
     * @param aBundle
     * @param aList
     * @param aKey
     */
    public static void putSerializableList(
        Bundle aBundle,
        ArrayList<? extends Serializable> aList,
        String aKey) {

        if (aList == null) {
            return;
        }

        aBundle.putInt(aKey + "size", aList.size());

        for (int i = 0; i < aList.size(); i++) {
            aBundle.putSerializable(aKey + i, aList.get(i));
        }
    }

    /**
     * Returns the value associated with the given key, or null if no mapping of the desired type
     * exists for the given key or a null value is explicitly associated with the key.
     *
     * @param aBundle
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getSerializableArrayList(
        Bundle aBundle,
        String key) {

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

    /**
     * Inserts a List of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.
     *
     * @param aBundle
     * @param aList
     * @param aKey
     */
    @SuppressWarnings("unchecked")
    public static void putParcelableList(
        Bundle aBundle,
        List<? extends Parcelable> aList,
        String aKey) {

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
