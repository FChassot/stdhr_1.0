package hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Models.Class.CulturalObjectType;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

/**
 * Created by chf on 01.09.2016.
 *
 * This helper class provides methods to calculate the distances useful for the radar module.
 */
public final class RadarHelper {

    // private constructor
    private RadarHelper() {}

    /**
     * This method analyses the response from the sparql request and converts this one into a list
     * of radarMarker
     *
     * @param aQueryResult The result of the query to be converted in a list of radarmarker
     * @param aCompassHeading
     * @param aCurrentUserLocation The current location of the app's user
     * @param aRadius The radius of the radar's search
     * @param aHeightView the actual height size of the view
     * @param aWidthView the actual widht size of the view
     *
     * @return A list of RadarMarker
     */
    public static List<RadarMarker> getRadarMarkersFromResponse(
        CitizenQueryResult aQueryResult,
        Float aCompassHeading,
        Location aCurrentUserLocation,
        double aRadius,
        int aHeightView,
        int aWidthView) {

        Checks.AssertNotNull(aQueryResult, "aQueryResult");

        List<RadarMarker> lMarkers = new ArrayList<>();
        List<RadarMarker> lLisOfMarkersFiltered = new ArrayList<>();

        for (CitizenDbObject lCulturalObject : aQueryResult.Results()) {
            //todo chf: removes when clarified
            for (RadarMarker lMarker : lLisOfMarkersFiltered) {
                if ((lMarker.getTitle().equals(lCulturalObject.GetValue("title"))) &&
                    (lMarker.getLongitude() == Double.parseDouble(lCulturalObject.GetValue("long"))) &&
                    (lMarker.getLatitude() == Double.parseDouble(lCulturalObject.GetValue("lat")))) {
                    continue;
                }
            }

            String lCulturalObjectId = lCulturalObject.GetValue("culturalInterest");
            String lTitle = lCulturalObject.GetValue("title");
            String lDescription = lCulturalObject.GetValue("description");
            double lCulturalObjectLatitude = Double.parseDouble(lCulturalObject.GetValue("lat"));
            double lCulturalObjectLongitude = Double.parseDouble(lCulturalObject.GetValue("long"));

            SpatialGeometryServices lSpatialGeometryServices = new SpatialGeometryServices();
            double lRadius = lSpatialGeometryServices.getRadiusInRadian(aCurrentUserLocation, (int)aRadius);

            //double lLatitudeMin = lCulturalObjectLatitude - lRadius;
            //double lLatitudeMax = lCulturalObjectLatitude + lRadius;
            //double lLongitudeMin = lCulturalObjectLongitude - lRadius;
            //double lLongitudeMax = lCulturalObjectLongitude + lRadius;

            RadarViewPosition lRadarViewPosition =
                calculateXYPositionOfTheMarkerInTheRadarView(
                    aHeightView,
                    aWidthView,
                    lCulturalObjectLatitude,
                    lCulturalObjectLongitude,
                    aCurrentUserLocation.getLatitude() - lRadius,
                    aCurrentUserLocation.getLatitude() + lRadius,
                    aCurrentUserLocation.getLongitude() - lRadius,
                    aCurrentUserLocation.getLongitude() + lRadius);

            RadarMarker lMarker =
                new RadarMarker(
                    lRadarViewPosition.getX(),
                    lRadarViewPosition.getY(),
                    lCulturalObjectLatitude,
                    lCulturalObjectLongitude,
                    Color.RED,
                    lTitle,
                    lCulturalObjectId,
                    lDescription);

            lMarkers.add(lMarker);
        }

        return lMarkers;
    }

    /**
     * This method extracts the subjects of a citizen sparql request
     *
     * @param aQueryResult
     * @return
     */
    public static List<String> getCulturalObjectSubjectFromResponse(
        CitizenQueryResult aQueryResult) {

        Checks.AssertNotNull(aQueryResult, "aQueryResult");

        List<String> lSubjects = new ArrayList<>();

        for (CitizenDbObject lObject : aQueryResult.Results()) {
            String lSubject = lObject.GetValue("subject");
            lSubjects.add(lSubject);
        }

        return lSubjects;
    }

    /**
     * This method analyses the response from the sparql server and convert this one into a list
     * of radarMarker
     *
     * @param aQueryResult the response of the sparql server
     *
     * @return a list of RadarMarker
     */
    public static ArrayList<CulturalObjectType> getRadarMarkersFromResponse(
        CitizenQueryResult aQueryResult) {

        // array list of type of cultural interest
        ArrayList<CulturalObjectType> lListOfCIType = new ArrayList<>();

        if (aQueryResult == null) {
            lListOfCIType.add(new CulturalObjectType("","Cultural place", false));
            lListOfCIType.add(new CulturalObjectType("","Cultural person", false));
            lListOfCIType.add(new CulturalObjectType("","Cultural event", false));
            lListOfCIType.add(new CulturalObjectType("","Folklore", false));
            lListOfCIType.add(new CulturalObjectType("","Physical object", false));
        }
        else {
            for (CitizenDbObject lCitizenObject : aQueryResult.Results()) {
                lListOfCIType.add(new CulturalObjectType("","cultural object conversion to do", false));
            }
        }

        return lListOfCIType;
    }

    /**
     * This method calculates the position of the marker in the view taking
     * into account the height and the width of the view.
     *
     * @param aHeightView the height of the view is necessary to calculate the position of the marker in the view
     * @param aWidthView the width of the view is necessary to calculate the position of the marker in the view
     * @param aCulturalObjectLatitude ...
     * @param aCulturalObjectLongitude ...
     * @param aMinLatitude ...
     * @param aMaxLatitude ...
     * @param aMinLongitude ...
     * @param aMaxLongitude ...
     *
     * @return The X, Y Positions in the view
     */
    private static RadarViewPosition calculateXYPositionOfTheMarkerInTheRadarView(
        int aHeightView,
        int aWidthView,
        double aCulturalObjectLatitude,
        double aCulturalObjectLongitude,
        double aMinLatitude,
        double aMaxLatitude,
        double aMinLongitude,
        double aMaxLongitude) {

        // first, calculate the position in X
        double lDeltaLongitude = (aMaxLongitude - aMinLongitude);
        double lDeltaLongitudeForOneUnitView = (lDeltaLongitude / aWidthView);

        double lObjectCulturalDeltaLongitude = (aCulturalObjectLongitude - aMinLongitude);
        double lPosX = (lObjectCulturalDeltaLongitude / lDeltaLongitudeForOneUnitView);

        // second, calculate the position in Y
        double lDeltaLatitude = (aMaxLatitude - aMinLatitude);
        double lDeltaLatitudeForOneUnitView = (lDeltaLatitude / aHeightView);

        double lObjectCulturalDeltaLatitude = (aMaxLatitude - aCulturalObjectLatitude);
        double lPosY = (lObjectCulturalDeltaLatitude / lDeltaLatitudeForOneUnitView);

        // instanciate a return object
        return new RadarViewPosition( (int)lPosX, (int)lPosY);
    }

    /**
     * This method gives the distance in the view between two points.
     * This method will be used to determine the nearest cultural objects compared
     * the citizen's user.
     *
     * @param aXPosition1OnScreen ...
     * @param aYPosition1OnScreen ...
     * @param aXPosition2OnScreen ...
     * @param aYPosition2OnScreen ...
     *
     * @return The distance between the two points
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static double calculateDistanceInTheViewBetweenTwoPoints(
            double aXPosition1OnScreen,
            double aYPosition1OnScreen,
            double aXPosition2OnScreen,
            double aYPosition2OnScreen) {

        double lDeltaX = Math.abs((aXPosition1OnScreen - aXPosition2OnScreen));
        double lDeltaY = Math.abs((aYPosition1OnScreen - aYPosition2OnScreen));
        double lTangente = (lDeltaY / lDeltaX); // Tangente = côté opposé / côté adjacent
        double lAngle = Math.atan(lTangente);

        double lDistance = (lDeltaY / Math.cos(lAngle));

        return lDistance;
    }

    /**
     * Get the GPS Location corresponding to a point touched on the view
     *
     * @param aXOnScreen
     * @param aYOnScreen
     *
     * @return
     */
    public static Location determineGpsPositionOnTheView(
        double aXOnScreen,
        double aYOnScreen,
        double aMinLatitude,
        double aMaxLatitude,
        double aMinLongitude,
        double aMaxLongitude,
        double aHeight,
        double aWidth) {

        double lDeltaLatitude = (aMaxLatitude - aMinLatitude);
        double lRapportY = (aHeight / aYOnScreen);
        double lY = aMinLatitude + (lDeltaLatitude / lRapportY);

        double lDeltaLongitude = (aMaxLongitude - aMinLongitude);
        double lRapportX = (aWidth / aXOnScreen);
        double lX = aMinLatitude + (lDeltaLongitude / lRapportX);

        Location lLocation = new Location(MyString.EMPTY_STRING);
        lLocation.setLongitude(lX);
        lLocation.setLatitude(lY);

        return lLocation;
    }

    // http://stackoverflow.com/questions/5314724/get-screen-coordinates-by-specific-location-and-longitude-android

    /**
     *
     * Since information on this topic is very sparse, and I recently solved this problem on the iPhone, I thought I would share my method for anyone that can make it work with Android (there's nothing really specific to iPhone in this answer except for the Math functions sin, cos, and fmod, which can be found in java.lang.Math). These are the steps I took:

     Obtain your own lat/lon and your current compass heading (lat1, lon1 and heading). On the iPhone, CLLocation returns these in degrees, but for these calculations they MUST be in radians (i.e. multiply by PI/180)
     Obtain lat/lon of Points of Interest (POI) in radians (lat2 and lon2).
     Calculate the distance between lat1/lon1 and lat2/lon2 using formula found here: http://www.movable-type.co.uk/scripts/latlong.html
     Calculate angle to lat2/lon2 in relation to north. This is also described in the link above but I had a little bit of trouble getting this to work, here is C code for this:

     double latDelta = (lat2 - lat1);
     double lonDelta = (lon2 - lon1);
     double y = sin(lonDelta)  * cos(lat2);
     double x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2)* cos(lonDelta);
     double angle = atan2(y, x); //not finished here yet
     double headingDeg = compass.currentHeading;
     double angleDeg = angle * 180/PI;
     double heading = headingDeg*PI/180;
     angle = fmod(angleDeg + 360, 360) * PI/180; //normalize to 0 to 360 (instead of -180 to 180), then convert back to radians
     angleDeg = angle * 180/PI;
     Using standard trigonometry, I calculate x and y. Remember, these coordinates are in 3D space, so we are not finished here yet because you still have to map them to 2D:

     x = sin(angle-heading) * distance;
     z = cos(angle-heading) * distance; //typically, z faces into the screen, but in our 2D map, it is a y-coordinate, as if you are looking from the bottom down on the world, like Google Maps
     Finally, using the projection formula, you can calculate screen x ( I didn't do y because it was not necessary for my project, but you would need to get accelerator data and figure out if the device is perpendicular to the ground). The projection formula is found here (scroll to the very bottom): http://membres.multimania.fr/amycoders/tutorials/3dbasics.html

     double screenX = (x * 256) / z
     Now you can use this x coordinate to move an image or a marker on your screen. Remember a few points:

     Everything must be in radians
     The angle from you to the POI relative to North is angleBeteweenPoints - currentHeading
     (For some reason I can't properly format the code on this computer, so if anyone wants to edit this answer, feel free).
     */
}
