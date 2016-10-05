package hesso.mas.stdhbtests;

import org.junit.Test;

import java.util.List;

import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;
import hesso.mas.stdhb.Client.Gui.Radar.RadarMarker;

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
                RadarHelper.getGreatCircleDistanceBetweenTwoPoints(
                    45,
                    45,
                    45,
                    45,
                    45,
                    45);

        assertEquals((int)lDistance, 0);
    }

    @Test
    public void get_markers_from_sparql_response() throws Exception {

        List<RadarMarker> lMarkers =
                RadarHelper.getRadarMarkersFromResponse(
                        (float)45.0,
                        null,
                        45.0,
                        null,
                        null);

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
                RadarHelper.calculateDistanceInTheViewBetweenTwoPoint(
                        0.0,
                        0.0,
                        45.0,
                        45.0);

        assertNotEquals(lDistance, 45);
    }
}
