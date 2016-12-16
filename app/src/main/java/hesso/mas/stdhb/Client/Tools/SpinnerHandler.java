package hesso.mas.stdhb.Client.Tools;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;
import java.util.NoSuchElementException;

import hesso.mas.stdhb.Base.Models.Basemodel;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

/**
 * Created by chf on 24.08.2016.
 *
 */
public class SpinnerHandler {

    /**
     * The method fill a spinner control with the given enum
     *
     * @param spinner the spinner control to fill
     * @param context interface to global information about the application environment.
     * @param resource int: The resource ID for a layout file containing a layout to use when instantiating views.
     */
    public static void fillComboByEnum(
        Spinner spinner,
        Context context,
        int resource) {

        ArrayAdapter lAdapter =
            new ArrayAdapter(
                context,
                resource,
                EnumClientServerCommunication.values());

        spinner.setAdapter(lAdapter);
    }

    /**
     * The method fill a spinner control with the possible subject contained in the Citizen DB.
     *
     * @param spinner the spinner control to fill
     * @param context Interface to global information about the application environment.
     * It allows access to application-specific resources and classes, as well as up-calls
     * for application-level operations such as launching activities, broadcasting and receiving
     * intents, etc.
     *
     * @param resource int: The resource ID for a layout file containing a layout to use when instantiating views.
     * @param values The objects to represent in the spinnerView.
     */
    public static void fillComboSubject(
        Spinner spinner,
        Context context,
        int resource,
        List<String> values) {

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        context,
                        resource,
                        values.toArray());

        spinner.setAdapter(lAdapter);
    }

    /**
     * Get the position of an item in the spinner control
     *
     * @param spinner the spinner control to fill
     * @param item
     *
     * @return
     * @throws NoSuchElementException
     */
    public static int getPositionByItem(
        Spinner spinner,
        String item) throws NoSuchElementException {

        final int count = spinner.getCount();

        for (int index = 0; index < count; index++) {
            if (item.equals(spinner.getItemAtPosition(index))) {
                return index;
            }
        }

        return Basemodel.NULL_KEY;
    }
}
