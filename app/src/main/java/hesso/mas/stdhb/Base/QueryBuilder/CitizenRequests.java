package hesso.mas.stdhb.Base.QueryBuilder;

import android.location.Location;

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

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                        "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                        "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
                        "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                        "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "SELECT ?culturalInterest ?longitude ?latitude WHERE {\n" +
                        "?culturalInterest rdfs:domain dbo:CulturalInterest .\n" +
                        "?latitude geo:lat ?lat .\n" +
                        "?longitude geo:long ?long .\n" +
                        "FILTER (?long > " + (lLongitude - aRadius) + " && ?long < " + (lLongitude + aRadius) + "&& \n" +
                        "?lat > " + (lLatitude - aRadius) + " && ?lat < " + (lLatitude + aRadius) + ")}";

        return lQuery;
    }

    /**
     * This method allows to do a specific search of Culturals objects
     *
     * @param aPlace
     * @param aPeriod
     *
     * @return The appropriate Query
     */
    public static String GetCulturalObjectQuery(
        String aPlace,
        String aPeriod) {

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "SELECT * WHERE {\n" +
                "?culturalInterest rdfs:domain dbo:CulturalInterest .\n" +
                "?x rdfs:domain ?Date .\n" +
                "?x rdfs:domain ?City .\n" +
                "FILTER (?hasEnd < " + aPeriod + " && ?hasBeginning > " + aPeriod + "&&\n" +
                "?City == " + aPlace + ")\n" +
                "}";

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
