package hesso.mas.stdhb.Client.GuiHandler;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;

/**
 * Created by chf on 24.08.2016.
 *
 *
 */
public class ComboBoxHelper {

    /**
     *
     * @param aCbo
     * @param aContext
     * @param aResource
     */
    public static void fillComboClientServerTechnology(
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
     *
     * @param aCbo
     * @param aContext
     * @param aResource
     */
    public static void fillComboSujet(
            Spinner aCbo,
            Context aContext,
            int aResource,
            List<CitizenDbObject> aValues) {

        ArrayAdapter lAdapter =
                new ArrayAdapter(
                        aContext,
                        aResource,
                        EnumClientServerCommunication.values());

        aCbo.setAdapter(lAdapter);
    };
}
