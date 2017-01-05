package hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Models.Class.CulturalObjectType;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;

/**
 * Created by chf on 01.09.2016.
 *
 * This helper class provides methods to calculate the distances useful for the radar module.
 * Reference: http://stackoverflow.com/questions/5314724/get-screen-coordinates-by-specific-location-and-longitude-android
 */
public final class RadarHelper {

    // Private constructor
    // The class is not instantiable
    private RadarHelper() {}

    /**
     * This method analyses the response from the sparql request and converts this one into a list
     * of radarMarker
     *
     * @param queryResult The result of the query to be converted in a list of radarmarker
     * @param currentDegree
     * @param currentUserLocation The current location of the app's user
     * @param radius The radius of the radar's search
     * @param heightView the actual height size of the view
     * @param widthView the actual width size of the view
     * @param movementMode Define if the radar must work with an automatic update of the position
     * of the cultural objects according to the north.
     *
     * @return A list of RadarMarker
     */
    public static List<RadarMarker> getRadarMarkersFromResponse(
        CityZenQueryResult queryResult,
        int currentDegree,
        Location currentUserLocation,
        double radius,
        int heightView,
        int widthView,
        boolean movementMode) {

        Checks.AssertNotNull(queryResult, "aQueryResult");

        List<RadarMarker> lMarkers = new ArrayList<>();
        List<RadarMarker> lLisOfMarkersFiltered = new ArrayList<>();

        for (CityZenDbObject lCulturalObject : queryResult.Results()) {
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
            double lRadius = lSpatialGeometryServices.getRadiusInRadian(currentUserLocation, (int)radius);

            RadarViewPosition lRadarViewPosition =
                calculateXYPositionOfTheMarkerInTheRadarView(
                    heightView,
                    widthView,
                    lCulturalObjectLatitude,
                    lCulturalObjectLongitude,
                    currentUserLocation.getLatitude() - lRadius,
                    currentUserLocation.getLatitude() + lRadius,
                    currentUserLocation.getLongitude() - lRadius,
                    currentUserLocation.getLongitude() + lRadius);

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

            if (movementMode) {
                int lCenter = (heightView / 2);

                RadarViewPosition lPositionAccordingCurrentDegree =
                        getRadarViewPositionForMarker(
                                lCenter,
                                lMarker,
                                currentDegree);

                lMarker.setPositionX(lPositionAccordingCurrentDegree.getX());
                lMarker.setPositionY(lPositionAccordingCurrentDegree.getY());
            }

            lMarkers.add(lMarker);
        }

        return lMarkers;
    }

    /**
     * This method calculates the new position of the marker according to the new
     * value of the azimuth.
     *
     * @param aMarker The center of the rotation
     * @param aMarker The marker
     * @param aAngle The current degree (Azimuth)
     */
    public static RadarViewPosition getRadarViewPositionForMarker(
            int aCenter,
            RadarMarker aMarker,
            double aAngle)  {

        int lX = aMarker.getPositionX();
        int lY = aMarker.getPositionY();

        double lAngle = 360 - aAngle;

        // The angles must be in radians

        // Formulas
        // X = BX+(AX−BX)cosϕ−(AY−BY)sinϕ
        // Y = BY+(AY−BY)cosϕ+(AX−BX)sinϕ

        double lAngleInRadians = Math.toRadians(lAngle);
        double lDeltaX = aCenter + ((lX-aCenter) * Math.cos(lAngleInRadians)) - ((lY-aCenter) * Math.sin(lAngleInRadians));
        double lDeltaY = aCenter + ((lY-aCenter) * Math.cos(lAngleInRadians)) + ((lX-aCenter) * Math.sin(lAngleInRadians));

        return new RadarViewPosition((int)lDeltaX, (int)lDeltaY);
    }

    /**
     * This method extracts the subjects of a citizen SPARQL request
     *
     * @param aQueryResult
     * @return
     */
    public static List<String> getCulturalObjectSubjectFromResponse(
        CityZenQueryResult aQueryResult) {

        Checks.AssertNotNull(aQueryResult, "aQueryResult");

        List<String> lSubjects = new ArrayList<>();

        for (CityZenDbObject lObject : aQueryResult.Results()) {
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
        CityZenQueryResult aQueryResult) {

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
            for (CityZenDbObject lCitizenObject : aQueryResult.Results()) {
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

        // First, calculate the position in X
        double lDeltaLongitude = (aMaxLongitude - aMinLongitude);
        double lDeltaLongitudeForOneUnitView = (lDeltaLongitude / aWidthView);

        double lObjectCulturalDeltaLongitude = (aCulturalObjectLongitude - aMinLongitude);
        double lPosX = (lObjectCulturalDeltaLongitude / lDeltaLongitudeForOneUnitView);

        // Second, calculate the position in Y
        double lDeltaLatitude = (aMaxLatitude - aMinLatitude);
        double lDeltaLatitudeForOneUnitView = (lDeltaLatitude / aHeightView);

        double lObjectCulturalDeltaLatitude = (aMaxLatitude - aCulturalObjectLatitude);
        double lPosY = (lObjectCulturalDeltaLatitude / lDeltaLatitudeForOneUnitView);

        // Instantiate an object to return
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
        double lTangent = (lDeltaY / lDeltaX);                          // Tangente = côté opposé / côté adjacent

        double lAngle = Math.atan(lTangent);

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

    /**
     * This method searches the nearest Cultural object according
     * the point touched on the screen by the user
     *
     * @param aOnTouchXCoordinate
     * @param aOnTouchYCoordinate
     *
     * @return
     */
    private RadarMarker findTheNearestCulturalObject(
            List<RadarMarker>aMarkers,
            float aOnTouchXCoordinate,
            float aOnTouchYCoordinate) {

        double lDistance = 0.0;

        RadarMarker lNearestMarker = null;

        if (aMarkers != null){
            for (RadarMarker lMarker : aMarkers) {
                double lHypotenuse =
                        RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                                aOnTouchXCoordinate,
                                aOnTouchYCoordinate,
                                lMarker.getPositionX(),
                                lMarker.getPositionY());

                if (lDistance == 0) {
                    lDistance = Math.abs(lHypotenuse);
                    lNearestMarker = lMarker;
                }

                if (lDistance != 0 && (Math.abs(lHypotenuse) < Math.abs(lDistance))) {
                    lNearestMarker = lMarker;
                    lDistance = Math.abs(lHypotenuse);
                }
            }
        }

        return lNearestMarker;
    }
}
