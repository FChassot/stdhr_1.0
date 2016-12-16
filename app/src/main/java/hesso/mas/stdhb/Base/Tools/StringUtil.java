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
     * @param string The string to evaluate
     *
     * @return yes if the string is null or blank
     */
    public static boolean isNullOrBlank(String string)
    {
        return (string==null || string.trim().equals(""));
    }

    /**
     * This method cuts a string if this one is longer
     * than the max length
     *
     * @param string
     * @param maxLength the max length of the string
     *
     * @return The string with the specified length
     */
    public static String subString(
        String string, int maxLength) {

        if (isNullOrBlank(string)) {return MyString.EMPTY_STRING;}

        int lLength = string.length();

        if (lLength > maxLength) {return string.substring(0, maxLength);}
        return string;
    }

}
