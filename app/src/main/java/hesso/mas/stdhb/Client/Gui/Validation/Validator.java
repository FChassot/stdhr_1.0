package hesso.mas.stdhb.Client.Gui.Validation;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

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

        if (aPlace.equals(MyString.EMPTY_STRING) || aPlace.equals("Lieu")) {
            lValDescCollection.add("* A place has to be given!");
        }

        if (aPeriod.equals(MyString.EMPTY_STRING) || aPeriod.equals("Période")) {
            if (lValDescCollection.count() > 0) {
                lValDescCollection.add("\n");
            }
            lValDescCollection.add("* A period has to be given!");
        }

        return lValDescCollection;
    }
}
