package hesso.mas.stdhb.Base.QueryBuilder;

import android.location.Location;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 11.05.2016.
 *
 *
 */
public final class CitizenRequests {

    // Constructor
    private CitizenRequests() {}

    /**
     *
     *
     * @param aLocation
     * @return
     */
    public static String CulturalObjectRadarQuery(
        Location aLocation) {

        String lQuery = MyString.EMPTY_STRING;

        Double lLongitude = aLocation.getLongitude();
        Double lLatitude = aLocation.getLatitude();

        lQuery += "SELECT ?culturalPerson WHERE {?x culturalPerson. ?y geo:lat ?lat " +
                "lat < " + lLatitude + "5}";

        return lQuery;
    }

    /**
     * This method allows to do a search of Culturals objects
     *
     * @param aPlace
     * @param aPeriod
     *
     * @return
     */
    public static String CulturalObjectSearchQuery(
            String aPlace,
            String aPeriod) {

        String lQuery = MyString.EMPTY_STRING;

        lQuery += "";

        return lQuery;
    }

    /**
     * This method allows to retrieve the typ of cultural objects available in
     * the Citizen triplestore.
     *
     * @return
     */
    public static String GetSearchObjectTyp() {

        return "SELECT DISTINCT ?CulturalInterest WHERE {?CulturalInterest a rdfs:comment.} LIMIT 100";
    }
}
