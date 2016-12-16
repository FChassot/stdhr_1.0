package hesso.mas.stdhb.DataAccess.QueryEngine.Sparql;

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
     * @param culturalObjectType The type of the cultural object to search
     * @param minLatitude The min latitude for the search of the cultural objects
     * @param maxLatitude The max latitude for the search of the cultural objects
     * @param minLongitude The min longitude for the search of the cultural objects
     * @param maxLongitude The max longitude for the search of the cultural objects
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectsInProximityQuery(
        String culturalObjectType,
        double minLatitude,
        double maxLatitude,
        double minLongitude,
        double maxLongitude,
        String subject,
        Integer limit) {

        //Checks.AssertNotEmpty(aCulturalObjectType, "aCulturalObjectType");

        String query;

        if (subject.equals(MyString.EMPTY_STRING)) {
            query =
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
                        "FILTER (xsd:double(?long) > " + minLongitude + " && xsd:double(?long) < " + maxLongitude + " && \n" +
                        "xsd:double(?lat) > " + minLatitude + " && xsd:double(?lat) < " + maxLatitude + ") .\n" +
                        "}\n" +
                        "LIMIT " + limit;
        }
        else {
            query =
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
                        "FILTER (xsd:double(?long) > " + minLongitude + " && xsd:double(?long) < " + maxLongitude + " && \n" +
                        "xsd:double(?lat) > " + minLatitude + " && xsd:double(?lat) < " + minLongitude + " && ?subject = '" + subject + "') .\n" +
                        "}\n" +
                        "LIMIT " + limit;
        }

        return query;
    }

    /**
     * A specific search of cultural objects can be retrieved with the following query
     *
     * @param title the title of the object to search
     * @param begin the begin date of the picture
     * @param end the end date of the picture
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectQueryByTitleAndDate(
        String title,
        int begin,
        int end,
        String subject) {

        Checks.AssertNotEmpty(title, "title");

        return
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                "prefix tm: <http://purl.org/dc/terms/>\n" +
                "prefix cr: <http://purl.org/dc/elements/1.1/creator>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "SELECT ?culturalInterest ?description ?title ?subject ?long ?lat ?image_url WHERE {\n" +
                "?culturalInterest dc:title ?title .\n" +
                "?culturalInterest tm:subject ?subject .\n" +
                "?culturalInterest dc:description ?description .\n" +
                "?culturalInterest geo:location ?x .\n" +
                "?culturalInterest dc:creator ?creator .\n" +
                "?cAggregator edm:aggregatedCHO ?CulturalInterest .\n" +
                "?cAggregator edm:hasView ?digitalrepresentation .\n" +
                "?digitalrepresentation tm:hasPart ?digitalitem .\n" +
                "?digitalitem dbo:image_url ?image_url .\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "filter (?subject = '" + subject + "' && regex(?title, '" + title + "', 'i')) . }\n" +
                "LIMIT 1\n";

        // ceci fonctionnne filter (?subject = "Mountain" && regex(?title, "Louvie", "i")) . }

        //"' && ?date " + aBegin + " && ?date < " + aEnd +

                /*"?x owl:hasEnd ?End .\n" +
                "?x owl:hasBeginning ?Begin .\n" +
                "?x dbo:City ?City .\n" +
                "FILTER (?End < " + aEnd + "&& ?Begin > " + aBegin + "&&\n" +
                " ?City == " + aPlace + ". })";*/
    }

    /**
     * A specific search of cultural objects can be retrieved with the following query
     *
     * @param title the title of the picture
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectInfoByObjectIdQuery(
        String title,
        String culturalInterest) {

        Checks.AssertNotEmpty(title, "title");

        String query =
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
                "filter (?culturalInterest = <" + culturalInterest + ">) . }\n" +
                "LIMIT 1\n";

        return query;
    }

    /**
     * A specific search of the types off cultural objects available in the
     * Citizen Endpoint can be retrieved with the following query
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCulturalObjectTypeQuery() {

        String query =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT DISTINCT ?culturalObjectType ?class \n" +
            "WHERE {?culturalObjectType rdfs:subClassOf ?class.\n" +
            "}";

        return query;
    }

    /**
     * A specific search of the types of cultural objects available in the
     * Citizen Endpoint can be retrieved with the following query
     *
     * @return a SPARQL query
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getSubjectQuery() {

        String query =
            "prefix tm: <http://purl.org/dc/terms/>\n" +
                "SELECT distinct ?subject WHERE {\n" +
                "?culturalObject tm:subject ?subject .\n" +
                "}";

        return query;
    }
}
