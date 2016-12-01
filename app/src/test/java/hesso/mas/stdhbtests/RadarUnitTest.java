package hesso.mas.stdhbtests;

import android.graphics.Color;

import org.junit.Test;

import java.util.List;

import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarViewPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by chf on 28.09.2016.
 *
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RadarUnitTest {
    @Test
    public void calculate_distance_between_two_points_isCorrect() throws Exception {

        double lDistance =
                SpatialGeometryServices.getGreatCircleDistanceBetweenTwoPoints(
                    45,
                    45,
                    45,
                    45,
                    45,
                    45);

        assertEquals((int)lDistance, 0);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void get_markers_from_sparql_response() throws Exception {

        List<RadarMarker> lMarkers =
                RadarHelper.getRadarMarkersFromResponse(
                        null,
                        45,
                        null,
                        45.0,
                        900,
                        900,
                        false);

        assertNotNull(lMarkers);
    }

    /**
     * This unit test checks if the method to calculate the distance between two points
     * still correctly work.
     *
     * @throws Exception
     */
    @Test
    public void calculate_distance_in_the_view_between_two_points() throws Exception {

        double lDistance =
                RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                        0.0,
                        0.0,
                        45.0,
                        45.0);

        assertNotEquals(lDistance, 45);
    }

    /**
     * This unit test checks if the method to calculate the new coordinates of a marker
     * after a rotation works
     * @throws Exception
     */
    @Test
    public void get_RadarView_Position_For_Marker() throws Exception {

        RadarMarker lMarker =
                new RadarMarker(
                    550,
                    550,
                    45.0,
                    45.0,
                    Color.RED,
                    "",
                    "",
                    "");

        RadarViewPosition lPosition1 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        lMarker,
                        360.0);

        assertEquals((int)lPosition1.getX(), 550);
        assertEquals((int)lPosition1.getY(), 550);

        RadarViewPosition lPosition2 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        lMarker,
                        90.0);

        assertEquals((int)lPosition2.getX(), 550);
        assertEquals((int)lPosition2.getY(), 350);

        RadarViewPosition lPosition3 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        lMarker,
                        180.0);

        assertEquals((int)lPosition3.getX(), 350);
        assertEquals((int)lPosition3.getY(), 350);

        RadarViewPosition lPosition4 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        lMarker,
                        270.0);

        assertEquals((int)lPosition4.getX(), 350);
        assertEquals((int)lPosition4.getY(), 550);

    }
}
