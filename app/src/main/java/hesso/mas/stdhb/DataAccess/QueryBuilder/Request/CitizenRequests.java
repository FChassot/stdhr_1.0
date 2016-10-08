package hesso.mas.stdhb.DataAccess.QueryBuilder.Request;

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
     * Points around a given co-ordinate can be retrieved with the following query
     *
     * @param aCulturalObjectType The type of the cultural object to search
     * @param aMinLatitude The current Location of the mobile
     * @param aMaxLatitude The current Location of the mobile
     * @param aMinLongitude The current Location of the mobile
     * @param aMaxLongitude The current Location of the mobile
     *
     * @return The appropriate Query
     */
    public static String getCulturalObjectsInProximityQuery(
        String aCulturalObjectType,
        double aMinLatitude,
        double aMaxLatitude,
        double aMinLongitude,
        double aMaxLongitude) {

        Checks.AssertNotEmpty(aCulturalObjectType);

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
                "prefix tm: <http://purl.org/dc/terms/>\n" +
                "select ?culturalInterest ?title ?subject ?description ?lat ?long where {\n" +
                "?culturalInterest tm:subject ?subject .\n" +
                "?culturalInterest dc:title ?title .\n" +
                "?culturalInterest dc:description ?description .\n" +
                "?culturalInterest geo:location ?x .\n" +
                "?x geo:long ?long .\n" +
                "?x geo:lat ?lat .\n" +
                "FILTER ( ?long > '" + aMinLongitude + "' && ?long < '" + aMaxLongitude + "' && \n" +
                "?lat > '" + aMinLatitude + "' && ?lat < '" + aMaxLatitude + "') .\n" +
                "}\n" +
                "LIMIT 100";

        return lQuery;
    }

    /**
     * This method allows to do a specific search of Culturals objects
     *
     * @param aTitle the title of the object to search
     * @param aBegin the begin date of the picture
     * @param aEnd the end date of the picture
     *
     * @return The appropriate Query
     */
    public static String getCulturalObjectQuery(
        String aTitle,
        Date aBegin,
        Date aEnd) {

        Checks.AssertNotEmpty(aTitle);

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

        return lQuery;
    }

    /**
     * This method allows to do a specific search of Culturals objects
     *
     * @param aTitle the title of the picture
     * @param aBegin tne begin date of the picture
     * @param aEnd the end date of the picture
     *
     * @return The appropriate Query
     */
    public static String getUniqueCulturalObjectInfoQuery(
            String aTitle,
            String aCulturalInterest,
            Date aBegin,
            Date aEnd) {

        Checks.AssertNotEmpty(aTitle);

        String lQuery =
                "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
                        "prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                        "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
                        "prefix edm: <http://www.europeana.eu/schemas/edm#>\n" +
                        "prefix tm: <http://purl.org/dc/terms/>\n" +
                        "prefix cr: <http://purl.org/dc/elements/1.1/creator>\n" +
                        "SELECT ?culturalInterest ?title ?subject ?description ?long ?lat ?image_url WHERE {\n" +
                        "?culturalInterest dc:title ?title .\n" +
                        "?culturalInterest tm:subject ?subject .\n" +
                        "?culturalInterest dc:description ?description .\n" +
                        "?culturalInterest geo:location ?x .\n" +
                        "?cAggregator edm:aggregatedCHO ?culturalInterest .\n" +
                        "?cAggregator edm:hasView ?drepr .\n" +
                        "?drepr tm:hasPart ?ditem .\n" +
                        "?ditem dbo:image_url ?image_url.\n" +
                        "?x geo:long ?long .\n" +
                        "?x geo:lat ?lat .\n" +
                        "filter (?culturalInterest = <" + aCulturalInterest + ">) . }\n" +
                        "LIMIT 1\n";

        return lQuery;
    }

    /**
     * This method allows to retrieve the typ of cultural objects available in
     * the Citizen triplestore.
     *
     * @return The appropriate Query
     */
    public static String getCulturalObjectTypeQuery() {

        String lQuery =
            "prefix dbo: <http://www.hevs.ch/datasemlab/cityzen/schema#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "prefix dc: <http://purl.org/dc/elements/1.1/>\n" +
            "SELECT DISTINCT ?culturalInterestType \n" +
            "WHERE {?culturalInterestType rdfs:subClassOf ?x.\n" +
            "?culturalInterestType dc:comment ?comment}";

        return lQuery;
    }

    /**
     * This method allows to retrieve the typ of cultural objects available in
     * the Citizen triplestore.
     *
     * @return The appropriate Query
     */
    public static String getSujetQuery() {

        String lQuery =
            "prefix tm: <http://purl.org/dc/terms/>\n" +
                "SELECT distinct ?subject WHERE {\n" +
                "?culturalObject tm:subject ?subject .\n" +
                "}";

        return lQuery;
    }
}
