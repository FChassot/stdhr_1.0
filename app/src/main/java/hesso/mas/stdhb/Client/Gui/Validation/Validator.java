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
            String aPeriod,
            String aSubject) {

        ValidationDescCollection lValDescCollection = new ValidationDescCollection();

        if (aSubject.equals(MyString.EMPTY_STRING)) {
            lValDescCollection.add("* A subject has to be choosen!");
        }

        if (aPlace.equals(MyString.EMPTY_STRING) || aPlace.equals("Place")) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            lValDescCollection.add("* A place has to be given!");
        }

        if (aPeriod.equals(MyString.EMPTY_STRING) || aPeriod.equals("Period")) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            //Resources.getSystem().getString(R.string.not_valid_place
            lValDescCollection.add("* A period has to be given!");
        }

        if (!aPeriod.equals(MyString.EMPTY_STRING)) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }

            if (aPeriod.length() != 9) {
                lValDescCollection.add("* The dateformat must be {year{}-year{}}!");
            }
        }

        return lValDescCollection;
    }
}
