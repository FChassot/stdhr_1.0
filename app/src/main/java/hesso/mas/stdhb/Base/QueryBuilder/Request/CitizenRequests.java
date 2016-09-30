package hesso.mas.stdhb.Base.QueryBuilder.Request;

import android.location.Location;

import java.util.Date;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Constants.BaseConstants;

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
     * @param aCurrentUserLocation The current Location of the mobile
     * @param aRadius The radius of search
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectsInProximityQuery(
        String aCulturalInterestType,
        Location aCurrentUserLocation,
        Integer aRadius) {

        //Checks.AssertNotEmpty(aCulturalInterestType);
        Checks.AssertNotNull(aCurrentUserLocation);

        Double lCurrentLongitude = aCurrentUserLocation.getLongitude();
        Double lCurrentLatitude = aCurrentUserLocation.getLatitude();

        Double lRadiusInKm = Double.parseDouble(aRadius.toString()) / 1000;
        Double lLatDegree = Double.parseDouble(BaseConstants.Attr_Lat_Degree);
        Double lLatDelta = lLatDegree / lRadiusInKm;

        Double lRadius = 0.1 / lLatDelta;

        Double lMinLongitude = (lCurrentLongitude - lRadius);
        Double lMaxLongitude = (lCurrentLongitude + lRadius);;
        Double lMinLatitude = (lCurrentLatitude - lRadius);
        Double lMaxLatitude = (lCurrentLatitude + lRadius);

        String lQuery =
                "prefix cti: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                "select ?title ?lat ?long where {\n" +
                "?culturalInterest dc:title ?title .\n" +
                "?culturalInterest geo:location ?x .\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "FILTER ( ?long > '" + lMinLongitude + "' && ?long < '" + lMaxLongitude + "' && \n" +
                "?lat > '" + lMinLatitude + "' && ?lat < '" + lMaxLatitude + "') .\n" +
                "}\n" +
                "LIMIT 100";

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

        /*String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix data: <http://www.hevs.ch/datasemlab/cityzen/data#>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "SELECT * WHERE {\n" +
                "?x dbo:CulturalInterest ?y .\n" +
                "?x owl:hasEnd ?End .\n" +
                "?x owl:hasBeginning ?Begin .\n" +
                "?x dbo:City ?City .\n" +
                "FILTER (?End < " + aEnd + "&& ?Begin > " + aBegin + "&&\n" +
                " ?City == " + aPlace + ". })";*/

        /*String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "SELECT ?z WHERE {\n" +
                "?x dbo:image_url ?z.\n" +
                "}\n" +
                "LIMIT 100";*/

        /*prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>
        prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
        prefix dc: <http://purl.org/dc/elements/1.1/>
        prefix edm: <http://www.europeana.eu/schemas/edm#>
        prefix tm: <http://purl.org/dc/terms/>
        prefix cr: <http://purl.org/dc/elements/1.1/creator>
        SELECT ?CPlace ?sujet ?long ?lat ?drepr ?ditem ?imageUrl WHERE {
            ?CPlace dc:title ?title .
                    ?CPlace tm:subject ?sujet .
                    ?CPlace geo:location ?x .
                    ?CPlace dc:creator ?creator .
                    ?CAgg edm:hasView ?drepr .
                    ?drepr tm:hasPart ?ditem .
                    ?ditem dbo:image_url ?imageUrl .
                    ?x geo:long ?long .
                    ?x geo:lat ?lat .
        }
        LIMIT 100*/

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
            "SELECT ?CPlace ?sujet ?long ?lat ?imagePreview WHERE {\n" +
                "?CPlace dc:title ?title .\n" +
                "?CPlace tm:subject ?sujet .\n" +
                "?CPlace geo:location ?x .\n" +
                "?CPlace dc:creator ?creator .\n" +
                "?CAgg edm:hasView ?drepr .\n" +
                "?drepr tm:hasPart ?ditem .\n" +
                "?ditem dbo:image_url ?imagePreview.\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "}\n" +
                "LIMIT 100\n";

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
