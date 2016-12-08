package hesso.mas.stdhb.Client.Gui.Validation;

import android.content.res.Resources;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 30.09.2016.
 *
 * This class validates the parameter before a Citizen search
 */
public class Validator {

    /**
     * This method validates the entries given before a search of the
     * Citizen database
     *
     * @param aPlace
     * @param aPeriod
     * @return
     */
    public static ValidationDescCollection ValidateSearch(
            String aPlace,
            String aPeriod) {

        ValidationDescCollection lValDescCollection = new ValidationDescCollection();

        if (aPlace.equals(MyString.EMPTY_STRING) || aPlace.equals(Resources.getSystem().getString(R.string.place))) {
            lValDescCollection.add(Resources.getSystem().getString(R.string.not_valid_place));
        }

        if (aPeriod.equals(MyString.EMPTY_STRING) || aPeriod.equals(Resources.getSystem().getString(R.string.periode))) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            lValDescCollection.add(Resources.getSystem().getString(R.string.not_valid_place));
        }

        return lValDescCollection;
    }
}
