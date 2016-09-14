package hesso.mas.stdhb.Base.QueryBuilder;

import android.location.Location;

import java.util.Date;

import hesso.mas.stdhb.Base.Checks.Checks;

/**
 * Created by chf on 11.05.2016.
 *
 * This class contains all the necessary Requests for this application.
 */
public final class CitizenRequests {

    // Constructor
    private CitizenRequests() {}

    /**
     *  return "select distinct ?Concept where {[] a ?Concept} LIMIT 10";
     * Points around a given co-ordinate can be retrieved with the following query :
     *
     * @param aCulturalObjectTypeOfSearch
     * @param aCurrentUserLocation
     * @param aRadius
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectsInProximityQuery(
        String aCulturalObjectTypeOfSearch,
        Location aCurrentUserLocation,
        Integer aRadius) {

        Checks.AssertNotEmpty(aCulturalObjectTypeOfSearch);
        Checks.AssertNotNull(aCurrentUserLocation);

        Double lLongitude = aCurrentUserLocation.getLongitude();
        Double lLatitude = aCurrentUserLocation.getLatitude();

        /**
         *         String lQuery =
         "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
         "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
         "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
         "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
         "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
         "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
         "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing>\n" +
         "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
         "SELECT * WHERE {\n" +
         "?culturalObject a dbo:CulturalPlace .\n" +
         "?culturalObject geo:lat ?lat .\n" +
         "?culturalObject geo:long ?long .\n" +
         "FILTER (?long > " + (lLongitude - aRadius) + " && ?long < " + (lLongitude + aRadius) + "&& \n" +
         "?lat > " + (lLatitude - aRadius) + " && ?lat < " + (lLatitude + aRadius) + ")} LIMIT 10";
         */

/*        String lQuery =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix data: <http://www.hevs.ch/datasemlab/cityzen/data#>\n" +
            "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "select * where {\n" +
            "?x dbo:CulturalEvent ?y .\n" +
            "?x geo:location ?location .\n" +
            "?location geo:lat ?lat .\n" +
            "?location geo:long ?long .\n" +
            "filter (?long > " + (lLongitude - aRadius) + " && ?long < " + (lLongitude + aRadius) + "&& \n" +
            "?lat > " + (lLatitude - aRadius) + " && ?lat < " + (lLatitude + aRadius) + ")} LIMIT 10";*/

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "select ?x ?lat ?long where {\n" +
                "?x geo:long ?long.\n" +
                "?x geo:lat ?lat.}\n" +
                "filter (?long > " + (lLongitude - aRadius) + " && ?long < " + (lLongitude + aRadius) + "&& \n" +
                "?lat > " + (lLatitude - aRadius) + " && ?lat < " + (lLatitude + aRadius) + ")} LIMIT 1";

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
                " ?City == " + aPlace + ")}";*/

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix dta: <http://www.hevs.ch/datasemlab/cityzen/data#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "select ?x ?y ?z where {\n" +
                "?x ?y ?z} limit 1000";

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
               "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
               "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
               "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
               "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
               "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
               "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
               "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
               "SELECT DISTINCT ?culturalInterestType \n" +
               "WHERE {?culturalInterestType rdfs:domain <http://purl.org/dc/elements/1.1/type>}";

        return lQuery;
    }
}
