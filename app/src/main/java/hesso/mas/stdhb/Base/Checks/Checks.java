package hesso.mas.stdhb.Base.Checks;

import android.support.annotation.NonNull;

import hesso.mas.stdhb.Base.Tools.StringUtil;

/**
 * Description: Generic class
 * Collection of Check methods allowing the validation of parameters.
 * Each of those method throws an Exception, wenn the ...
 *
 * @author chf
 * @version Version 1.0
 * @since 08.09.2016
 */
public final class Checks<T> {

    /**
     * Private constructor
     * The class is not instantiable
     */
    private Checks() {}

    /**
     * Checks that the parameter isn't null.
     *
     * @param object The object that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertNotNull(T object, @NonNull String parameterName)
    {
        if (object == null) {
            throw new RuntimeException("The Object " + parameterName + " cannot be null.");
        }
    }

    /**
     * Checks that the parameter isn't null.
     *
     * @param string The string that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertNotEmpty(String string, @NonNull String parameterName)
    {
        if (StringUtil.isNullOrBlank(string)) {
            throw new RuntimeException("The variable " + parameterName + " mustn't be empty.");
        }
    }

    /**
     * Checks that the parameter bigger or equal than 0.
     *
     * @param value The value that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertIsStrictPositive(int value, @NonNull String parameterName)
    {
        if (value <= 0) {
            throw new RuntimeException("The variable " + parameterName + " must be positive.");
        }
    }

    /**
     * Checks that the parameter bigger or equal than 0.
     *
     * @param value The value that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertIsStrictPositive(double value, @NonNull String parameterName)
    {
        if (value <= 0) {
            throw new RuntimeException("The variable " + parameterName + " must be positive.");
        }
    }
}
