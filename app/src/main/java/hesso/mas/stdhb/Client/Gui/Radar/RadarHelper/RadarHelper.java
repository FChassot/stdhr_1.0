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
     * @param currentDegree ...
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

        Checks.AssertNotNull(queryResult, "queryResult");

        List<RadarMarker> markers = new ArrayList<>();
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

            markers.add(lMarker);
        }

        return markers;
    }

    /**
     * This method calculates the new position of the marker according to the new
     * value of the azimuth.
     *
     * @param center The center of the rotation
     * @param marker The marker
     * @param angle The current degree (Azimuth)
     */
    public static RadarViewPosition getRadarViewPositionForMarker(
            int center,
            RadarMarker marker,
            double angle)  {

        int lX = marker.getPositionX();
        int lY = marker.getPositionY();

        double lAngle = 360 - angle;

        // The angles must be in radians

        // Formulas
        // X = BX+(AX−BX)cosϕ−(AY−BY)sinϕ
        // Y = BY+(AY−BY)cosϕ+(AX−BX)sinϕ

        double angleInRadians = Math.toRadians(lAngle);
        double deltaX = center + ((lX-center) * Math.cos(angleInRadians)) - ((lY-center) * Math.sin(angleInRadians));
        double deltaY = center + ((lY-center) * Math.cos(angleInRadians)) + ((lX-center) * Math.sin(angleInRadians));

        return new RadarViewPosition((int)deltaX, (int)deltaY);
    }

    /**
     * This method extracts the subjects of a cityZen SPARQL request
     *
     * @param queryResult result of the query to handle
     *
     * @return a list of the different subjects
     */
    public static List<String> getCulturalObjectSubjectFromResponse(
        CityZenQueryResult queryResult) {

        Checks.AssertNotNull(queryResult, "queryResult");

        List<String> subjects = new ArrayList<>();

        for (CityZenDbObject lObject : queryResult.Results()) {
            String subject = lObject.GetValue("subject");
            subjects.add(subject);
        }

        return subjects;
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
        double deltaLongitude = (aMaxLongitude - aMinLongitude);
        double deltaLongitudeForOneUnitView = (deltaLongitude / aWidthView);

        double objectCulturalDeltaLongitude = (aCulturalObjectLongitude - aMinLongitude);
        double posX = (objectCulturalDeltaLongitude / deltaLongitudeForOneUnitView);

        // Second, calculate the position in Y
        double deltaLatitude = (aMaxLatitude - aMinLatitude);
        double deltaLatitudeForOneUnitView = (deltaLatitude / aHeightView);

        double objectCulturalDeltaLatitude = (aMaxLatitude - aCulturalObjectLatitude);
        double posY = (objectCulturalDeltaLatitude / deltaLatitudeForOneUnitView);

        // Instantiate an object to return
        return new RadarViewPosition( (int)posX, (int)posY);
    }

    /**
     * This method gives the distance in the view between two points.
     * This method will be used to determine the nearest cultural objects compared
     * the cityZen's user.
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

        double deltaX = Math.abs((aXPosition1OnScreen - aXPosition2OnScreen));
        double deltaY = Math.abs((aYPosition1OnScreen - aYPosition2OnScreen));
        double tangent = (deltaY / deltaX);                          // Tangente = côté opposé / côté adjacent

        double angle = Math.atan(tangent);

        double distance = (deltaY / Math.cos(angle));

        return distance;
    }

    /**
     * Get the GPS Location corresponding to a point touched on the view
     *
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

        double deltaLatitude = (aMaxLatitude - aMinLatitude);
        double rapportY = (aHeight / aYOnScreen);
        double lY = aMinLatitude + (deltaLatitude / rapportY);

        double deltaLongitude = (aMaxLongitude - aMinLongitude);
        double rapportX = (aWidth / aXOnScreen);
        double lX = aMinLatitude + (deltaLongitude / rapportX);

        Location location = new Location(MyString.EMPTY_STRING);
        location.setLongitude(lX);
        location.setLatitude(lY);

        return location;
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

        double distance = 0.0;

        RadarMarker nearestMarker = null;

        if (aMarkers != null){
            for (RadarMarker lMarker : aMarkers) {
                double lHypotenuse =
                        RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                                aOnTouchXCoordinate,
                                aOnTouchYCoordinate,
                                lMarker.getPositionX(),
                                lMarker.getPositionY());

                if (distance == 0) {
                    distance = Math.abs(lHypotenuse);
                    nearestMarker = lMarker;
                }

                if (distance != 0 && (Math.abs(lHypotenuse) < Math.abs(distance))) {
                    nearestMarker = lMarker;
                    distance = Math.abs(lHypotenuse);
                }
            }
        }

        return nearestMarker;
    }
}
