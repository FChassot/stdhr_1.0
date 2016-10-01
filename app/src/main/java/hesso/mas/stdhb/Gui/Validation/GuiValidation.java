package hesso.mas.stdhb.Gui.Validation;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

/**
 * Created by chf on 30.09.2016.
 */
public class GuiValidation {

    public static ValidationDescCollection ValidateSearch(String aPlace, String aPeriod) {

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
