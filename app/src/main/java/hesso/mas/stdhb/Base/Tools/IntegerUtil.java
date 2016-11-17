package hesso.mas.stdhb.Base.Tools;

import static java.lang.Math.floor;

/**
 * Created by chf on 13.10.2016.
 *
 */
public final class IntegerUtil {

    // private constructor
    private IntegerUtil() {}

    /**
     * The method try to cast the string aValue to an int.
     *
     * @param aValue The String Value to cast
     *
     * @return true when the value has been correctly casted.
     */
    public static boolean tryParseInt(String aValue) {
        try {
            Integer.parseInt(aValue);
            return true;
        }
        catch (NumberFormatException aExc) {
            return false;
        }
    }

    /**
     * The method rounds the integer value to the decimal
     *
     * @param aIntValue
     *
     * @return
     */
    public static double roundToDecimal(int aIntValue) {
        return floor(aIntValue / 10) * 10;
    }
}
