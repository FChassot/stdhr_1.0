package hesso.mas.stdhb.Base.Checks;

import android.support.annotation.NonNull;

import hesso.mas.stdhb.Base.Tools.StringUtil;

/**
 * Created by chf on 08.09.2016.
 *
 * Generic class
 * Collection of Check methods allowing the validation of parameters.
 * Each of those method throws an Exception, wenn the ...
 */
public final class Checks<T> {

    /**
     * Private constructor
     * The class is not instanciable
     */
    private Checks() {}

    /**
     * Checks that the parameter isn't null.
     *
     * @param aObject The object that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertNotNull(T aObject, @NonNull String aParameterName)
    {
        if (aObject == null) {
            throw new RuntimeException("The Object " + aParameterName + " cannot be null.");
        }
    }

    /**
     * Checks that the parameter isn't null.
     *
     * @param aString The string that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertNotEmpty(String aString, @NonNull String aParameterName)
    {
        if (StringUtil.isNullOrBlank(aString)) {
            throw new RuntimeException("The variable " + aParameterName + " mustn't be empty.");
        }
    }


}
