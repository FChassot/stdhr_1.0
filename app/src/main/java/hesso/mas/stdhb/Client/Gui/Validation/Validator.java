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
            lValDescCollection.add("* Please select a category!");
        }

        if (aPlace.equals(MyString.EMPTY_STRING) || aPlace.equals("Keyword")) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            lValDescCollection.add("* Please give a keyword!");
        }

        if (aPeriod.equals(MyString.EMPTY_STRING) || aPeriod.equals("Period")) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            //Resources.getSystem().getString(R.string.not_valid_place
            lValDescCollection.add("* Please give a period!");
        }

        if (!aPeriod.equals(MyString.EMPTY_STRING)) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }

            if (aPeriod.length() != 9) {
                lValDescCollection.add("* Period must be in format <yyyy-yyyy>!");
            }
        }

        return lValDescCollection;
    }
}
