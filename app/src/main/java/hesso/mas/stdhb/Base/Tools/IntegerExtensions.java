package hesso.mas.stdhb.Base.Tools;

/**
 * Created by chf on 13.10.2016.
 */
public class IntegerExtensions {

    /**
     * The method try to cast the string aValue to an int.
     *
     * @param aValue
     *
     * @return true when the value hast been correctly casted.
     */
    public static boolean tryParseInt(String aValue) {
        try {
            Integer.parseInt(aValue);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}
