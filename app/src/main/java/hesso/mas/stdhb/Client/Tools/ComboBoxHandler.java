package hesso.mas.stdhb.Client.Tools;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

/**
 * Created by chf on 24.08.2016.
 *
 * Hinweis: die Klasse muss nach und nach so umgestellt werden, dass die Methoden nicht mehr
 * statisch angeboten werden, sondern dass es sich nur noch um Instanzmethoden handelt, welche
 * durch das entsprechende Interface definiert werden
 *
 */
public class ComboBoxHandler {

    /**
     * The method fill a spinner control with the given enum
     *
     * @param aCbo the spinner control to fill
     * @param aContext interface to global information about the application environment.
     * @param aResource
     */
    public static void fillComboByEnum(
        Spinner aCbo,
        Context aContext,
        int aResource) {

        ArrayAdapter lAdapter =
            new ArrayAdapter(
                aContext,
                aResource,
                EnumClientServerCommunication.values());

        aCbo.setAdapter(lAdapter);
    };

    /**
     * The method fill a spinner control with the possible subject contained in the Citizen DB.
     *
     * @param aCbo the spinner control to fill
     * @param aContext Interface to global information about the application environment.
     * It allows access to application-specific resources and classes, as well as up-calls
     * for application-level operations such as launching activities, broadcasting and receiving intents, etc.
     * @param aResource
     * @param aValues
     */
    public static void fillComboSubject(
        Spinner aCbo,
        Context aContext,
        int aResource,
        List<String> aValues) {

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        aContext,
                        aResource,
                        aValues.toArray());

        aCbo.setAdapter(lAdapter);
    };
}