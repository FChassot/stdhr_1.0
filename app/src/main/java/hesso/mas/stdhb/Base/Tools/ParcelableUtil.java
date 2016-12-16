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

    // Private constructor
    private ParcelableUtil() {}

    /**
     * Inserts a Serializable value into the mapping of this Bundle, replacing any existing value
     * for the given key. Either key or value may be null.
     *
     * @param bundle
     * @param aList
     * @param aKey
     */
    public static void putSerializableList(
        Bundle bundle,
        ArrayList<? extends Serializable> aList,
        String aKey) {

        if (aList == null) {
            return;
        }

        bundle.putInt(aKey + "size", aList.size());

        for (int i = 0; i < aList.size(); i++) {
            bundle.putSerializable(aKey + i, aList.get(i));
        }
    }

    /**
     * Returns the value associated with the given key, or null if no mapping of the desired type
     * exists for the given key or a null value is explicitly associated with the key.
     *
     * @param bundle
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getSerializableArrayList(
        Bundle bundle,
        String key) {

        int lSize = bundle.getInt(key + "size", -1);

        if (lSize < 0) {
            return null;
        }

        ArrayList<T> result = new ArrayList<>();

        for (int i = 0; i < lSize; i++) {
            result.add((T) bundle.getSerializable(key + i));
        }

        return result;
    }

    /**
     * Inserts a List of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.
     *
     * @param bundle
     * @param list
     * @param key
     */
    @SuppressWarnings("unchecked")
    public static void putParcelableList(
        Bundle bundle,
        List<? extends Parcelable> list,
        String key) {

        if (list == null) {
            return;
        }

        ArrayList<? extends Parcelable> arrayList;

        if (list instanceof ArrayList) {
            arrayList = (ArrayList<? extends Parcelable>) list;
        } else {
            arrayList = new ArrayList<>(list);
        }

        bundle.putParcelableArrayList(key, arrayList);
    }
}
