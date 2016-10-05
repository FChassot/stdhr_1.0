package hesso.mas.stdhb.DataAccess.QueryEngine.Request;

import android.location.Location;

import java.util.Date;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

/**
 * Created by chf on 11.05.2016.
 *
 * This class contains all the necessary Requests for this application.
 */
public final class CitizenRequests {

    // Constructor
    private CitizenRequests() {}

    /**
     * Points around a given co-ordinate can be retrieved with the following query
     *
     * @param aCulturalInterestType
     * @param aMinLatitude The current Location of the mobile
     * @param aMaxLatitude The current Location of the mobile
     * @param aMinLongitude The current Location of the mobile
     * @param aMaxLongitude The current Location of the mobile
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectsInProximityQuery(
        String aCulturalInterestType,
        double aMinLatitude,
        double aMaxLatitude,
        double aMinLongitude,
        double aMaxLongitude) {

        Checks.AssertNotEmpty(aCulturalInterestType);

        //Location lObject = new Location(MyString.EMPTY_STRING);
        //lObject.setLongitude(aCurrentUserLocation.getLongitude());
        //lObject.setLatitude(aCurrentUserLocation.getLatitude());
        //lObject.setLatitude(lMaxLatitude);
        //lObject.setLongitude(lMinLongitude);

        /*double lDistance =
                RadarHelper.getGreatCircleDistanceBetweenTwoPoints(
                aCurrentUserLocation.getLatitude(),
                lObject.getLatitude(),
                aCurrentUserLocation.getLongitude(),
                lObject.getLongitude(),
                0,
                0);*/

        String lQuery =
                "prefix citizen: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                "select ?title ?lat ?long where {\n" +
                "?culturalInterest dc:title ?title .\n" +
                "?culturalInterest geo:location ?x .\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "FILTER ( ?long > '" + aMinLongitude + "' && ?long < '" + aMaxLongitude + "' && \n" +
                "?lat > '" + aMinLatitude + "' && ?lat < '" + aMaxLatitude + "') .\n" +
                "}\n" +
                "LIMIT 1";

        return lQuery;
    }

    /**
     * This method allows to do a specific search of Culturals objects
     *
     * @param aPlace
     * @param aBegin
     * @param aEnd
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectQuery(
        String aPlace,
        Date aBegin,
        Date aEnd) {

        /**
         * CPlace	sujet	long	lat	imagePreview
         http://www.hevs.ch/datasemlab/cityzen/data#9dec8165-50ff-4e66-a208-9c9db66ae880	Nature, landscape	7.6245001	46.256119	https: //cave.valais-wallis-digital.ch/media/filer_public/c7/da/c7da19b7-5f79-4a1b-9706-d44721cb065a/9dec8165-50ff-4e66-a208-9c9db66ae880.jpg
         */
        String lQuery =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
            "prefix tm: <http://purl.org/dc/terms/>\n" +
            "prefix cr: <http://purl.org/dc/elements/1.1/creator>\n" +
            "SELECT ?culturalPlace ?sujet ?long ?lat ?imagePreview WHERE {\n" +
                "?culturalPlace dc:title ?title .\n" +
                "?culturalPlace tm:subject ?sujet .\n" +
                "?culturalPlace geo:location ?x .\n" +
                "?culturalPlace dc:creator ?creator .\n" +
                "?CAgg edm:hasView ?drepr .\n" +
                "?drepr tm:hasPart ?ditem .\n" +
                "?ditem dbo:image_url ?imagePreview.\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "}\n" +
                "LIMIT 100\n";

                        /*"?x owl:hasEnd ?End .\n" +
                "?x owl:hasBeginning ?Begin .\n" +
                "?x dbo:City ?City .\n" +
                "FILTER (?End < " + aEnd + "&& ?Begin > " + aBegin + "&&\n" +
                " ?City == " + aPlace + ". })";*/

        return lQuery;
    }

    /**
     * This method allows to retrieve the typ of cultural objects available in
     * the Citizen triplestore.
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectsTypeQuery() {

        String lQuery =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT DISTINCT ?culturalInterestType \n" +
            "WHERE {?culturalInterestType rdfs:subClassOf ?x.\n" +
            "?culturalInterestType dc:comment ?comment}";

        return lQuery;
    }
}
