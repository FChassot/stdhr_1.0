package hesso.mas.stdhb.Base.Tools;

import static java.lang.Math.floor;

/**
 * Created by chf on 13.10.2016.
 *
 * Util class for the integer type
 */
public final class IntegerUtil {

    // Private constructor
    private IntegerUtil() {}

    /**
     * The method try to cast the string aValue to an int.
     *
     * @param value The String Value to cast
     *
     * @return true when the value has been correctly casted.
     */
    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException aExc) {
            return false;
        }
    }

    /**
     * The method rounds the integer value to the decimal
     *
     * @param intValue
     *
     * @return
     */
    public static double roundToDecimal(int intValue) {
        return floor(intValue / 10) * 10;
    }
}
