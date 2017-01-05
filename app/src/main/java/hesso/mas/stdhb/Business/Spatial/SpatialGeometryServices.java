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
     * @param radiusSearch the radius of search in meters
     *
     * @return the radius of search in radians
     */
    public double getRadiusInRadian(
        Location currentUserLocation,
        int radiusSearch) {

        double radiusInKm = (radiusSearch / 1000);             // convert to kilometers

        double distanceInMetersForOneLatitudeDegree =
            getDistanceForOneLatitudeDegree(
                    currentUserLocation);

        double latDelta = (distanceInMetersForOneLatitudeDegree/1000) / radiusInKm;

        return 1 / latDelta;
    }

    /**
     * This method calculate the number of meters who corresponds to one degree
     *
     * @param currentUserLocation the current location of the app's user
     *
     * @return the distance in meters between the current latitude and this one
     * with a difference of one degree
     */
    public double getDistanceForOneLatitudeDegree(
        Location currentUserLocation) {

        double distance =
            getDistanceBetweenTwoPoints(
                currentUserLocation.getLatitude(),
                currentUserLocation.getLatitude() + 1,
                currentUserLocation.getLongitude(),
                currentUserLocation.getLongitude(),
                0,
                0);

        return distance;
    }

    /**
     * This method calculate the number of meters who corresponds to one degree
     *
     * @param currentUserLocation the current location of the app's user
     *
     * @return the distance in meters between the current longitude and this one
     * with a difference of one degree
     */
    public double getDistanceForOneLongitudeDegree(
        Location currentUserLocation) {

        double distance =
            getDistanceBetweenTwoPoints(
                currentUserLocation.getLatitude(),
                currentUserLocation.getLatitude(),
                currentUserLocation.getLongitude(),
                currentUserLocation.getLongitude() + 1,
                0,
                0);

        return distance;
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
        double latitude1,
        double latitude2,
        double longitude1,
        double longitude2,
        double elevation1,
        double elevation2) {

        double latitudeDistance = Math.toRadians(latitude2 - latitude1);
        double longitudeDistance = Math.toRadians(longitude2 - longitude1);

        double lA =
            Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
            + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
            * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double lC = 2 * Math.atan2(Math.sqrt(lA), Math.sqrt(1 - lA));

        double distance = EARTH_RADIUS_KM * lC * 1000;   // convert to meters

        double height = elevation1 - elevation2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
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
     * @param latitude1 the latitude of the source point
     * @param longitude1 the longitude of the source point
     * @param latitude2 the latitude of the destination point
     * @param longitude2 the longitude of the destination point
     * @return the relative bearing from point 1 to point 2, in degrees. The result is guaranteed
     *         to fall in the range 0-360
     */
    public static float getBearing(
        double latitude1,
        double longitude1,
        double latitude2,
        double longitude2) {

        latitude1 = Math.toRadians(latitude1);
        longitude1 = Math.toRadians(longitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude2 = Math.toRadians(longitude2);

        double lon = longitude2 - longitude1;

        double y = Math.sin(lon) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1)
                * Math.cos(latitude2) * Math.cos(lon);

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
