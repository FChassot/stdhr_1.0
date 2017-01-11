package hesso.mas.stdhbtests;

import android.graphics.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
@RunWith(MockitoJUnitRunner.class)
public class RadarUnitTest {

    @Test
    public void calculate_distance_between_two_points_isCorrect() throws Exception {

        double distance =
                SpatialGeometryServices.getDistanceBetweenTwoPoints(
                    45,
                    45,
                    45,
                    45,
                    45,
                    45);

        assertEquals((int)distance, 0);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void get_markers_from_sparql_response() throws Exception {

        /*List<RadarMarker> markers =
                RadarHelper.getRadarMarkersFromResponse(
                        null,
                        45,
                        null,
                        45.0,
                        900,
                        900,
                        false);

        assertNotNull(markers);*/
    }

    /**
     * This unit test checks if the method to calculate the distance between two points
     * still correctly work.
     *
     * @throws Exception
     */
    @Test
    public void calculate_distance_in_the_view_between_two_points() throws Exception {

        double distance =
                RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                        0.0,
                        0.0,
                        45.0,
                        45.0);

        assertNotEquals(distance, 45);
    }

    /**
     * This unit test checks if the method to calculate the new coordinates of a marker
     * after a rotation works
     * @throws Exception
     */
    @Test
    public void get_RadarView_Position_For_Marker() throws Exception {

        RadarMarker marker =
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
                        marker,
                        360.0);

        assertEquals((int)lPosition1.getX(), 550);
        assertEquals((int)lPosition1.getY(), 550);

        RadarViewPosition lPosition2 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        marker,
                        90.0);

        //assertEquals((int)lPosition2.getX(), 550);
        //assertEquals((int)lPosition2.getY(), 350);

        RadarViewPosition lPosition3 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        marker,
                        180.0);

        //assertEquals((int)lPosition3.getX(), 350);
        //assertEquals((int)lPosition3.getY(), 350);

        RadarViewPosition lPosition4 =
                RadarHelper.getRadarViewPositionForMarker(
                        600,
                        marker,
                        270.0);

        //assertEquals((int)lPosition4.getX(), 350);
        //assertEquals((int)lPosition4.getY(), 550);

    }
}
