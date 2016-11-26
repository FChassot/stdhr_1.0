package hesso.mas.stdhb.Base.Tools;

/**
 * Created by chf on 09.11.2016.
 *
 */
public final class StringUtil {

    // Private constructor
    private StringUtil() {}

    /**
     * Indicates whether the specified string is null or an Empty string.
     *
     * @param aString The string to evaluate
     *
     * @return yes if the string is null or blank
     */
    public static boolean isNullOrBlank(String aString)
    {
        return (aString==null || aString.trim().equals(""));
    }
}
