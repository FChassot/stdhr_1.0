package hesso.mas.stdhb.Gui.Radar;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.Tools.MyString;

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
     *
     * @param aResponse
     *
     * @return A list of RadarMarker
     */
    public static List<RadarMarker> GetRadarMarkersFromReponse(String aResponse) {
        List<RadarMarker> lMarkers = new ArrayList<RadarMarker>();;
        return lMarkers;
    }

    /**
     * This method calculates the position of the RadarMarker in the view.
     *
     * @param aRadarDimension
     * @param aMobileLocation
     * @param aRadarMarker
     *
     * @return The X, Y Positions in the view
     */
    private String GetXYMarkerPositionInTheRadarView(
            Integer aRadarDimension,
            Location aMobileLocation,
            RadarMarker aRadarMarker
    ) {
        return MyString.EMPTY_STRING;
    }

    /*
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference.
     *
     * The Haversine method as its base.
     *
     * aLatitude1, aLongitude1 Start point aLatitude2, aLongitude2 End point aElevation1 Start altitude in meters
     * aElevation2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double GetDistanceBetweenTwoPoints(
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
}
