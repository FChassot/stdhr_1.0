package hesso.mas.stdhb.Gui.Radar;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.QueryBuilder.Response.CitizenDbObject;
import hesso.mas.stdhb.Base.QueryBuilder.Response.CitizenQueryResult;

/**
 * Created by chf on 01.09.2016.
 *
 * This helper class provides methods to calculate the distances useful
 * for the radar function.
 */
public final class RadarHelper {

    // private constructor
    private RadarHelper() {}

    /**
     *
     * @param aCompassHeading
     * @param aQueryResult
     *
     * @return A list of RadarMarker
     */
    public static List<RadarMarker> GetRadarMarkersFromResponse(
        Float aCompassHeading,
        CitizenQueryResult aQueryResult) {

        List<RadarMarker> lMarkers = new ArrayList<RadarMarker>();

        if (aQueryResult != null && aQueryResult.Count() > 0) {
            for (CitizenDbObject lCulturalInterestObject : aQueryResult.Results()) {
                RadarViewPosition lRadarViewPosition =
                        GetXYPositionOfTheMarkerInTheRadarView(
                                900,
                                900,
                                null,
                                Double.parseDouble(lCulturalInterestObject.GetValue("lat")),
                                Double.parseDouble(lCulturalInterestObject.GetValue("long")));

                RadarMarker lMarker =
                        new RadarMarker(
                                lRadarViewPosition.getX(),
                                lRadarViewPosition.getY(),
                                Color.RED);

                lMarkers.add(lMarker);
            }
        }

        return lMarkers;
    }

    /**
     * This method calculates the position of the RadarMarker in the view.
     *
     * @param aHeightView
     * @param aWithView
     * @param aMobileLocation
     * @param aLatitude
     * @param aLongitude
     *
     * @return The X, Y Positions in the view
     */
    private static RadarViewPosition GetXYPositionOfTheMarkerInTheRadarView(
        Integer aHeightView,
        Integer aWithView,
        Location aMobileLocation,
        Double aLatitude,
        Double aLongitude) {

        return new RadarViewPosition(800, 800);
    }

    /*
     * Calculates the distance between two points in latitude and longitude taking
     * into account height difference.
     *
     * The Haversine formula is used to calculate the great-circle distance between two points –
     * that is, the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’ distance
     * between the points (ignoring any hills they fly over, of course!).
     *
     * @Param aLatitude1, aLongitude1 Start point aLatitude2, aLongitude2 End point aElevation1 Start altitude in meters
     * aElevation2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double GetGreatCircleDistanceBetweenTwoPoints(
        double aLatitude1,
        double aLatitude2,
        double aLongitude1,
        double aLongitude2,
        double aElevation1,
        double aElevation2) {

        final int lRadiusEarth = 6371; // Radius of the earth

        Double lLatitudeDistance = Math.toRadians(aLatitude2 - aLatitude1);
        Double lLongitudeDistance = Math.toRadians(aLongitude2 - aLongitude1);

        Double a = Math.sin(lLatitudeDistance / 2) * Math.sin(lLatitudeDistance / 2)
                + Math.cos(Math.toRadians(aLatitude1)) * Math.cos(Math.toRadians(aLatitude2))
                * Math.sin(lLongitudeDistance / 2) * Math.sin(lLongitudeDistance / 2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double lDistance = lRadiusEarth * c * 1000; // convert to meters

        double lHeight = aElevation1 - aElevation2;

        lDistance = Math.pow(lDistance, 2) + Math.pow(lHeight, 2);

        return Math.sqrt(lDistance);
    }

    /**
     *
     * @param aSensorManager
     * @return
     */
    public Float GetCurrentCompassHeading(SensorManager aSensorManager) {

        return 0.0F;
    };

    /**
     *
     * @param aLocation
     * @return
     */
    public String GetRadiansLatLongOfCulturalObject(Location aLocation) {
        return null;
    }

    /**
     * Calculate angle to lat2/lon2 in relation to north.
     * This is also described in the link above but I had a little bit of trouble getting this to work, here is C code for this:
     *
     *
     * @return Angle
     */
    public double GetAngleFromTwoPoints(
        double lat1,
        double lat2,
        double long1,
        double long2,
        double aCurrentHeading) {

        double lLatDelta = (lat2 - lat1);
        double lonDelta = (long2 - long1);
        double y = Math.sin(lonDelta)  * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2)* Math.cos(lonDelta);
        double angle = Math.atan2(y, x); //not finished here yet
        double headingDeg = aCurrentHeading;
        double angleDeg = angle * 180/Math.PI;
        double heading = headingDeg*Math.PI/180;
        //angle = fmod(angleDeg + 360, 360) * Math.PI/180; //normalize to 0 to 360 (instead of -180 to 180), then convert back to radians
        angleDeg = angle * 180/Math.PI;

        return angleDeg;
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
