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
     * @param aValue
     * @param aPlaces
     *
     * @return
     */
    public static double round(double aValue, int aPlaces) {
        if (aPlaces < 0) throw new IllegalArgumentException();

        BigDecimal lBigDecimalValue = new BigDecimal(aValue);
        lBigDecimalValue = lBigDecimalValue.setScale(aPlaces, RoundingMode.HALF_UP);

        return lBigDecimalValue.doubleValue();
    }
}
