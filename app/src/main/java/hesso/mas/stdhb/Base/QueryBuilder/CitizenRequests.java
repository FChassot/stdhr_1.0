package hesso.mas.stdhb.Base.QueryBuilder;

import android.location.Location;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;

/**
 * Created by chf on 11.05.2016.
 *
 *
 */
public final class CitizenRequests {

    // Constructor
    private CitizenRequests() {}

    /**
     *  return "select distinct ?Concept where {[] a ?Concept} LIMIT 10";
     *
     * @param aLocation
     * @param aCulturalObjectTypeOfSearch
     * @param aSearchRadius
     *
     * @return
     */
    public static String GetCulturalObjectsInProximity(
        String aCulturalObjectTypeOfSearch,
        String aLocation,
        Integer aSearchRadius) {

        Checks.AssertNotEmpty(aCulturalObjectTypeOfSearch);
        //Checks.AssertNotNull(aLocation);

        String lLongitude = aLocation; //aLocation.getLongitude();
        String lLatitude = aLocation; //aLocation.getLatitude();

        String lQuery =
                "prefix : <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?CulturalObject " +
                "FROM <http://www.hevs.ch/datasemlab/cityzen/schema>\n" +
                "WHERE {\n" +
                "?culturalObject rdfs:domain <http://purl.org/dc/elements/1.1/> .\n" +
                "?culturalObjectLocation geo:lat ?latitude .\n" +
                "?culturalObjectLocation geo:lon ?longitude .\n" +
                "FILTER (?latitude < " + lLatitude + aSearchRadius + ") && \n" +
                "?longitude < " + lLongitude + aSearchRadius + ")\n" +
                "}";

        return lQuery;
    }

    /**
     * This method allows to search Culturals objects
     *
     * @param aPlace
     * @param aPeriod
     *
     * @return
     */
    public static String GetCulturalObject(
        String aPlace,
        String aPeriod) {

        String lQuery =
                "prefix : <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT * " +
                "FROM <http://www.hevs.ch/datasemlab/cityzen/schema>\n" +
                "WHERE {\n" +
                "?culturalObject rdfs:domain <http://purl.org/dc/elements/1.1/> .\n" +
                "?culturalObject rdfs:domain ?Date .\n" +
                "?culturalObject rdfs:domain ?Place .\n" +
                "FILTER (?Date > " + aPeriod + ") && \n" +
                "?Place == " + aPlace + ")\n" +
                "}";

        return lQuery;
    }

    /**
     * This method allows to retrieve the typ of cultural objects available in
     * the Citizen triplestore.
     *
     * @return
     */
    public static String GetCulturalObjectsTyp() {

        String lQuery =
               "prefix : <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
               "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
               "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
               "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
               "prefix xml: <http://www.w3.org/XML/1998/namespace>\n" +
               "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
               "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
               "SELECT DISTINCT ?culturalObjectType " +
               "FROM <http://www.hevs.ch/datasemlab/cityzen/schema>\n" +
               "WHERE {?culturalObjectType rdfs:domain <http://purl.org/dc/elements/1.1/type>} LIMIT  10";

        return lQuery;
    }
}
