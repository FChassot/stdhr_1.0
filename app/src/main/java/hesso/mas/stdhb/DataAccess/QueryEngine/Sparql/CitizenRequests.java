package hesso.mas.stdhb.DataAccess.QueryEngine.Sparql;

import java.util.Date;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Tools.MyString;

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
     * @param aCulturalObjectType The type of the cultural object to search
     * @param aMinLatitude The min latitude for the search of the cultural objects
     * @param aMaxLatitude The max latitude for the search of the cultural objects
     * @param aMinLongitude The min longitude for the search of the cultural objects
     * @param aMaxLongitude The max longitude for the search of the cultural objects
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectsInProximityQuery(
        String aCulturalObjectType,
        double aMinLatitude,
        double aMaxLatitude,
        double aMinLongitude,
        double aMaxLongitude,
        String aSubject,
        Integer aLimit) {

        //Checks.AssertNotEmpty(aCulturalObjectType, "aCulturalObjectType");

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

        String lQuery;

        if (aSubject.equals(MyString.EMPTY_STRING)) {
            lQuery =
                    "prefix citizen: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                    "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                    "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                    "prefix tm: <http://purl.org/dc/terms/>\n" +
                    "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select ?culturalInterest ?title ?subject ?description ?lat ?long where {\n" +
                        "?culturalInterest tm:subject ?subject .\n" +
                        "?culturalInterest dc:title ?title .\n" +
                        "?culturalInterest dc:description ?description .\n" +
                        "?culturalInterest geo:location ?x .\n" +
                        "?x geo:long ?long .\n" +
                        "?x geo:lat ?lat .\n" +
                        "FILTER (xsd:double(?long) > " + aMinLongitude + " && xsd:double(?long) < " + aMaxLongitude + " && \n" +
                        "xsd:double(?lat) > " + aMinLatitude + " && xsd:double(?lat) < " + aMaxLatitude + ") .\n" +
                        "}\n" +
                        "LIMIT " + aLimit;
        }
        else {
            lQuery =
                    "prefix citizen: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                    "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                    "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                    "prefix tm: <http://purl.org/dc/terms/>\n" +
                    "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "select ?culturalInterest ?title ?subject ?description ?lat ?long where {\n" +
                        "?culturalInterest tm:subject ?subject .\n" +
                        "?culturalInterest dc:title ?title .\n" +
                        "?culturalInterest dc:description ?description .\n" +
                        "?culturalInterest geo:location ?x .\n" +
                        "?x geo:long ?long .\n" +
                        "?x geo:lat ?lat .\n" +
                        "FILTER (xsd:double(?long) > " + aMinLongitude + " && xsd:double(?long) < " + aMaxLongitude + " && \n" +
                        "xsd:double(?lat) > " + aMinLatitude + " && xsd:double(?lat) < " + aMaxLatitude + " && ?subject = '" + aSubject + "') .\n" +
                        "}\n" +
                        "LIMIT " + aLimit;
        }

        return lQuery;
    }

    /**
     * A specific search of cultural objects can be retrieved with the following query
     *
     * @param aTitle the title of the object to search
     * @param aBegin the begin date of the picture
     * @param aEnd the end date of the picture
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectQuery(
        String aTitle,
        Date aBegin,
        Date aEnd) {

        Checks.AssertNotEmpty(aTitle, "aTitle");

        return
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
            "prefix tm: <http://purl.org/dc/terms/>\n" +
            "prefix cr: <http://purl.org/dc/elements/1.1/creator>\n" +
            "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
            "SELECT ?CulturalInterest ?title ?subject ?long ?lat ?image_url WHERE {\n" +
                "?CulturalInterest dc:title ?title .\n" +
                "?CulturalInterest tm:subject ?subject .\n" +
                "?CulturalInterest geo:location ?x .\n" +
                "?CulturalInterest dc:creator ?creator .\n" +
                "?cAggregator edm:aggregatedCHO ?CulturalInterest .\n" +
                "?cAggregator edm:hasView ?digitalrepresentation .\n" +
                "?digitalrepresentation tm:hasPart ?digitalitem .\n" +
                "?digitalitem dbo:image_url ?image_url .\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "}\n" +
                "LIMIT 1\n";

                /*"?x owl:hasEnd ?End .\n" +
                "?x owl:hasBeginning ?Begin .\n" +
                "?x dbo:City ?City .\n" +
                "FILTER (?End < " + aEnd + "&& ?Begin > " + aBegin + "&&\n" +
                " ?City == " + aPlace + ". })";*/
    }

    /**
     * A specific search of cultural objects can be retrieved with the following query
     *
     * @param aTitle the title of the picture
     * @param aBegin tne begin date of the picture
     * @param aEnd the end date of the picture
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getUniqueCulturalObjectInfoQuery(
        String aTitle,
        String aCulturalInterest,
        Date aBegin,
        Date aEnd) {

        Checks.AssertNotEmpty(aTitle, "aTitle");

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix tm: <http://purl.org/dc/terms/>\n" +
                "prefix cr: <http://purl.org/dc/elements/1.1/creator>\n" +
                "SELECT ?culturalInterest ?format ?title ?subject ?description ?long ?lat ?image_url WHERE {\n" +
                "?culturalInterest dc:title ?title .\n" +
                "?culturalInterest tm:subject ?subject .\n" +
                "?culturalInterest dc:description ?description .\n" +
                "?culturalInterest geo:location ?location .\n" +
                "?cAggregator edm:aggregatedCHO ?culturalInterest .\n" +
                "?cAggregator edm:hasView ?digitalrepresentation .\n" +
                "?digitalrepresentation tm:hasPart ?digitalitem .\n" +
                "?digitalrepresentation dc:format ?format .\n" +
                "?digitalitem dbo:image_url ?image_url.\n" +
                "?location geo:long ?long .\n" +
                "?location geo:lat ?lat .\n" +
                "filter (?culturalInterest = <" + aCulturalInterest + ">) . }\n" +
                "LIMIT 1\n";

        return lQuery;
    }

    /**
     * A specific search of the types off cultural objects available in the
     * Citizen Endpoint can be retrieved with the following query
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectTypeQuery() {

        String lQuery =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT DISTINCT ?culturalObjectType ?class \n" +
            "WHERE {?culturalObjectType rdfs:subClassOf ?class.\n" +
            "}";

        return lQuery;
    }

    /**
     * A specific search of the types off cultural objects available in the
     * Citizen Endpoint can be retrieved with the following query
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getSubjectQuery() {

        String lQuery =
            "prefix tm: <http://purl.org/dc/terms/>\n" +
                "SELECT distinct ?subject WHERE {\n" +
                "?culturalObject tm:subject ?subject .\n" +
                "}";

        return lQuery;
    }
}
