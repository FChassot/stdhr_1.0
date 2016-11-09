package hesso.mas.stdhb.Business.Spatial;

import android.location.Location;

/**
 * Created by chf on 13.10.2016.
 *
 * This class contains services for the spatial search.
 */
public class SpatialGeometryServices {

    /**
     * This method allows to search the radius of search in radians
     *
     * @param aRadiusSearch the radius of search in meters
     *
     * @return the radius of search in radians
     */
    public static double getRadiusInRadian(
        Location aCurrentUserLocation,
        int aRadiusSearch) {

        double lRadiusInKm = aRadiusSearch / 1000;      // convert to kilometers

        double lDistanceInMetersForOneLatitudeDegree =
            getDistanceForOneLatitudeDegree(aCurrentUserLocation);

        double lLatDelta = (lDistanceInMetersForOneLatitudeDegree/1000) / lRadiusInKm;

        return 1 / lLatDelta;
    }

    /**
     * This method calculate the number of meters who corresponds to one degree
     *
     * @param aCurrentUserLocation the current location of the app's user
     *
     * @return the distance in meters between the current latitude and this one
     * with a difference of one degree
     */
    public static double getDistanceForOneLatitudeDegree(
        Location aCurrentUserLocation) {

        double lDistance =
            getGreatCircleDistanceBetweenTwoPoints(
                aCurrentUserLocation.getLatitude(),
                aCurrentUserLocation.getLatitude() + 1,
                aCurrentUserLocation.getLongitude(),
                aCurrentUserLocation.getLongitude(),
                0,
                0);

        return lDistance;
    }

    /**
     * This method calculate the number of meters who corresponds to one degree
     *
     * @param aCurrentUserLocation the current location of the app's user
     *
     * @return the distance in meters between the current longitude and this one
     * with a difference of one degree
     */
    public static double getDistanceForOneLongitudeDegree(
        Location aCurrentUserLocation) {

        double lDistance =
            getGreatCircleDistanceBetweenTwoPoints(
                aCurrentUserLocation.getLatitude(),
                aCurrentUserLocation.getLatitude(),
                aCurrentUserLocation.getLongitude(),
                aCurrentUserLocation.getLongitude() + 1,
                0,
                0);

        return lDistance;
    }

    /*
     * Calculates the distance between two points in latitude and longitude taking
     * into account height difference.
     *
     * The Haversine formula is used to calculate the great-circle distance between
     * two points – that is, the shortest distance over the earth’s surface – giving
     * an ‘as-the-crow-flies’ distance between the points (ignoring any hills they
     * fly over, of course!).
     *
     * @Param aLatitude1, aLongitude1 Start point aLatitude2, aLongitude2 End point
     * aElevation1 Start altitude in meters aElevation2 End altitude in meters
     *
     * @returns the distance in meters
     */
    public static double getGreatCircleDistanceBetweenTwoPoints(
        double aLatitude1,
        double aLatitude2,
        double aLongitude1,
        double aLongitude2,
        double aElevation1,
        double aElevation2) {

        final int lRadiusOfEarth = 6371; // Radius of the earth

        double lLatitudeDistance = Math.toRadians(aLatitude2 - aLatitude1);
        double lLongitudeDistance = Math.toRadians(aLongitude2 - aLongitude1);

        double lA = Math.sin(lLatitudeDistance / 2) * Math.sin(lLatitudeDistance / 2)
                + Math.cos(Math.toRadians(aLatitude1)) * Math.cos(Math.toRadians(aLatitude2))
                * Math.sin(lLongitudeDistance / 2) * Math.sin(lLongitudeDistance / 2);

        double lC = 2 * Math.atan2(Math.sqrt(lA), Math.sqrt(1 - lA));

        double lDistance = lRadiusOfEarth * lC * 1000;   // convert to meters

        double lHeight = aElevation1 - aElevation2;

        lDistance = Math.pow(lDistance, 2) + Math.pow(lHeight, 2);

        return Math.sqrt(lDistance);
    }

}
