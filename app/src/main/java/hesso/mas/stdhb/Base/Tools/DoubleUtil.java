package hesso.mas.stdhb.Base.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by chf on 07.12.2016.
 */

public final class DoubleUtil {

    /**
     * This method allow to round a number to n decimal places
     *
     * @param value the number to round
     * @param places the number of places after the decimal
     *
     * @return true when the value has been correctly rounded.
     */
    public static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimalValue = new BigDecimal(value);
        bigDecimalValue = bigDecimalValue.setScale(places, RoundingMode.HALF_UP);

        return bigDecimalValue.doubleValue();
    }
}
