package hesso.mas.stdhb.Base.Tools;

/**
 * Created by chf on 13.10.2016.
 */
public final class IntegerExtensions {

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
}
