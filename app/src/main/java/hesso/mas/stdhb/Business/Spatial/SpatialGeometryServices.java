package hesso.mas.stdhb.Business.Spatial;

import android.location.Location;

/**
 * Created by chf on 13.10.2016.
 *
 * This class contains services for the spatial search.
 */
public class SpatialGeometryServices {

    // The Earth's radius, in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;

    // Constructor
    public SpatialGeometryServices() {}

    /**
     * This method allows to search the radius of search in radians
     *
     * @param aRadiusSearch the radius of search in meters
     *
     * @return the radius of search in radians
     */
    public double getRadiusInRadian(
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
    public double getDistanceForOneLatitudeDegree(
        Location aCurrentUserLocation) {

        double lDistance =
            getDistanceBetweenTwoPoints(
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
    public double getDistanceForOneLongitudeDegree(
        Location aCurrentUserLocation) {

        double lDistance =
            getDistanceBetweenTwoPoints(
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
    public static double getDistanceBetweenTwoPoints(
        double aLatitude1,
        double aLatitude2,
        double aLongitude1,
        double aLongitude2,
        double aElevation1,
        double aElevation2) {

        final int lRadiusOfEarth = 6371; // Radius of the earth

        double lLatitudeDistance = Math.toRadians(aLatitude2 - aLatitude1);
        double lLongitudeDistance = Math.toRadians(aLongitude2 - aLongitude1);

        double lA =
            Math.sin(lLatitudeDistance / 2) * Math.sin(lLatitudeDistance / 2)
            + Math.cos(Math.toRadians(aLatitude1)) * Math.cos(Math.toRadians(aLatitude2))
            * Math.sin(lLongitudeDistance / 2) * Math.sin(lLongitudeDistance / 2);

        double lC = 2 * Math.atan2(Math.sqrt(lA), Math.sqrt(1 - lA));

        double lDistance = lRadiusOfEarth * lC * 1000;   // convert to meters

        double lHeight = aElevation1 - aElevation2;

        lDistance = Math.pow(lDistance, 2) + Math.pow(lHeight, 2);

        return Math.sqrt(lDistance);
    }

    /**
     * Gets the great circle distance in kilometers between two geographical points, using
     * the <a href="http://en.wikipedia.org/wiki/Haversine_formula">haversine formula</a>.
     *
     * @param latitude1 the latitude of the first point
     * @param longitude1 the longitude of the first point
     * @param latitude2 the latitude of the second point
     * @param longitude2 the longitude of the second point
     * @return the distance, in kilometers, between the two points
     */
    public static float getDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double sqrtHaversineLat = Math.sin(dLat / 2);
        double sqrtHaversineLon = Math.sin(dLon / 2);
        double a = sqrtHaversineLat * sqrtHaversineLat + sqrtHaversineLon * sqrtHaversineLon
                * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (EARTH_RADIUS_KM * c);
    }

    /**
     * Gets the relative bearing from one geographical coordinate to another.
     *
     * @param aLatitude1 the latitude of the source point
     * @param aLongitude1 the longitude of the source point
     * @param aLatitude2 the latitude of the destination point
     * @param aLongitude2 the longitude of the destination point
     * @return the relative bearing from point 1 to point 2, in degrees. The result is guaranteed
     *         to fall in the range 0-360
     */
    public static float getBearing(
            double aLatitude1,
            double aLongitude1,
            double aLatitude2,
            double aLongitude2) {

        aLatitude1 = Math.toRadians(aLatitude1);
        aLongitude1 = Math.toRadians(aLongitude1);
        aLatitude2 = Math.toRadians(aLatitude2);
        aLongitude2 = Math.toRadians(aLongitude2);

        double dLon = aLongitude2 - aLongitude1;

        double y = Math.sin(dLon) * Math.cos(aLatitude2);
        double x = Math.cos(aLatitude1) * Math.sin(aLatitude2) - Math.sin(aLatitude1)
                * Math.cos(aLatitude2) * Math.cos(dLon);

        double bearing = Math.atan2(y, x);
        return mod((float) Math.toDegrees(bearing), 360.0f);
    }

    /**
     * Calculates {@code a mod b} in a way that respects negative values (for example,
     * {@code mod(-1, 5) == 4}, rather than {@code -1}).
     *
     * @param a the dividend
     * @param b the divisor
     * @return {@code a mod b}
     */
    public static int mod(int a, int b) {
        return (a % b + b) % b;
    }

    /**
     * Calculates {@code a mod b} in a way that respects negative values (for example,
     * {@code mod(-1, 5) == 4}, rather than {@code -1}).
     *
     * @param a the dividend
     * @param b the divisor
     * @return {@code a mod b}
     */
    public static float mod(float a, float b) {
        return (a % b + b) % b;
    }

}
