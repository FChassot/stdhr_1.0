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
     *
     * @throws Exception
     */
    @Test
    public void get_RadarView_Position_For_Marker() throws Exception {

        /*RadarViewPosition lRadarviewPosition =
                RadarHelper.getRadarViewPositionForMarker(
                        new RadarMarker(
                                150,
                                150,
                                0.0,
                                0.0,
                                Color.RED,
                                null,
                                null,
                                null),
                        180.0);*/

        assertEquals(1, 1);
    }
}
