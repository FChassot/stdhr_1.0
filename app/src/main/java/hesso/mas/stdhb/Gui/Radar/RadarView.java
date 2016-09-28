package hesso.mas.stdhb.Gui.Radar;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import hesso.mas.stdhb.Base.Constants.*;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Gui.GoogleMap.MapsActivity;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents the basic building block for user interface components.
 * A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
 * View is the base class for widgets, which are used to create interactive UI components (buttons, text fields, etc.).
 * The ViewGroup subclass is the base class for layouts,
 * which are invisible containers that hold other Views (or other ViewGroups) and define their layout properties.
 */
public class RadarView extends 	android.view.View {

        Handler mHandler = new android.os.Handler();

        private Context myContext;

        private List<RadarMarker> mMarkers;

        private final int POINT_ARRAY_SIZE = 35;

        private int fps = 100;

        float lAlpha = 0;

        Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
        Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];

    //region Constructors

    // Default constructor
    public RadarView(Context aContext) {

        this(aContext, null);
    }

    // Constructor
    public RadarView(
            Context aContext,
            AttributeSet aAttributeSet) {

        this(aContext, aAttributeSet, 0);
    }

    // Constructor
    public RadarView(
        Context aContext,
        AttributeSet aAttributes,
        int aDefStyleAttr) {

        super(aContext, aAttributes, aDefStyleAttr);

        myContext = aContext;

        Paint localPaint = new Paint();

        localPaint.setColor(Color.WHITE);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(5.0F);
        localPaint.setAlpha(0);

        int lAlpha_step = 255 / POINT_ARRAY_SIZE;

        for (int i=0; i < latestPaint.length; i++) {
            latestPaint[i] = new Paint(localPaint);
            latestPaint[i].setAlpha(255 - (i* lAlpha_step));
        }
    }

    //endregion

        Runnable mTick = new Runnable() {
            @Override
            public void run() {
                invalidate();
                mHandler.postDelayed(this, 1000 / fps);
            }
        };

        public synchronized List<RadarMarker> getMarkers() {
            return mMarkers;
        }

        /**
        * This method allows to update the markers received
         * by the radar.
        */
        public synchronized void updateMarkers(List<RadarMarker> aMarkers) {
            mMarkers = aMarkers;
        }

        /**
         * This method allows to start the animation
         */
        public void startRadarAnimation() {
            mHandler.removeCallbacks(mTick);
            mHandler.post(mTick);
        }

        /**
         * This method allows to stop the radar's animation
         */
        public void stopRadarAnimation() {
                mHandler.removeCallbacks(mTick);
            }

        /**
         * The most important step in drawing a custom view is to override the onDraw() method.
         * The parameter to onDraw() is a Canvas object that the view can use to draw itself.
         * The Canvas class defines methods for drawing text, lines, bitmaps, and many other graphics
         * primitives. You can use these methods in onDraw() to create your custom user interface (UI).
         *
         * Before you can call any drawing methods, though, it's necessary to create a Paint object.
         * The next section discusses Paint in more detail.
         *
         * @param aCanvas
         */
        @Override
        protected void onDraw(Canvas aCanvas) {
            super.onDraw(aCanvas);

            Paint lLocalPaint = latestPaint[0];

            int lCanvasWidth = this.getWidth();
            int lCanvasHeight = this.getHeight();

            int lMaxRayOfRadar = Math.min(lCanvasWidth, lCanvasHeight);

            int i = lMaxRayOfRadar / 2;
            int j = i - 1;

            this.DrawTheRadar(aCanvas, lLocalPaint, i, j);
            this.DrawTheMarkers(lLocalPaint, aCanvas, lMaxRayOfRadar);

            lAlpha -= 3;

            if (lAlpha < -360) lAlpha = 0;

            double lAngle = Math.toRadians(lAlpha);

            int lOffsetX =  (int) (i + (float)(i * Math.cos(lAngle)));
            int lOffsetY = (int) (i - (float)(i * Math.sin(lAngle)));

            latestPoint[0]= new Point(lOffsetX, lOffsetY);

            for (int x = POINT_ARRAY_SIZE-1; x > 0; x--) {
                latestPoint[x] = latestPoint[x-1];
            }

            for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
                Point point = latestPoint[x];

                if (point != null) {
                    aCanvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
                }
            }
        }

    //region Markers

    //endregion

    //region Design-radar

        /**
         * Draw the radar in the view
         *
         * @param aCanvas
         * @param aLocalPaint
         * @param i
         * @param j
         */
        public void DrawTheRadar(
            Canvas aCanvas,
            Paint aLocalPaint,
            int i,
            int j) {

            aCanvas.drawCircle(i, i, j, aLocalPaint);
            aCanvas.drawCircle(i, i, j-25, aLocalPaint);
            aCanvas.drawCircle(i, i, j * 3 / 4, aLocalPaint);
            aCanvas.drawCircle(i, i, j >> 1, aLocalPaint);
            aCanvas.drawCircle(i, i, j >> 2, aLocalPaint);

        }

        /**
         * Displays the markers in the view.
         *
         * @param aPaint
         * @param aMaxRadiusOfRadar
         */
        public void DrawTheMarkers(
            Paint aPaint,
            Canvas aCanvas,
            int aMaxRadiusOfRadar) {

            List<RadarMarker> lMarkers = getMarkers();

            if (lMarkers != null){
                if (lMarkers.size() > 0) {
                    for (RadarMarker lMarker : lMarkers) {
                        aCanvas.drawCircle(
                            lMarker.getPositionX(),
                            lMarker.getPositionY(),
                            (((aMaxRadiusOfRadar / 2) - 1) >> 5),
                            aPaint);

                        aPaint.setColor(lMarker.getColor());
                        aPaint.setStyle(Paint.Style.FILL);
                    }
                }

                aPaint.setStyle(Paint.Style.STROKE);
                aPaint.setColor(Color.WHITE);
            }
        }
    //endregion

    //region Help-methods

        /**
         * Called when a touch screen motion event occurs.
         *
         * @param event
         * @return
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float lXCoordinate = 0;
            float lYCoordinate = 0;

            if(event.getAction() == MotionEvent.ACTION_UP){
                lXCoordinate = event.getX();
                lYCoordinate = event.getY();
            }

            // Check if the click in the view has been done
            if (!isPointInsideView(lXCoordinate, lYCoordinate, this)) {
                return false;
            }

            Intent lIntent = new Intent(myContext, MapsActivity.class);

            Location lCoordinates = new Location(MyString.EMPTY_STRING);

            // GPS Coordinates
            lCoordinates.setAltitude(829);
            lCoordinates.setLatitude(46.6092369);
            lCoordinates.setLongitude(7.029020100000025);

            lIntent.putExtra(MapsActivity.RADAR_EXTRA, lCoordinates);

            RadarMarker lCulturalObject =
                    FindTheNearestCulturalObject(
                            lXCoordinate,
                            lYCoordinate);

            if (lCulturalObject != null) {
                LatLng lGpsCoordonates =
                        new LatLng(
                                Double.parseDouble(Float.toString(lCulturalObject.getPositionX())),
                                Double.parseDouble(Float.toString(lCulturalObject.getPositionY())));

                lIntent.putExtra(BaseConstants.Attr_Gps_Coordinates, lGpsCoordonates);

                myContext.startActivity(lIntent);

                return true;
            }

            return false;
        }

        private RadarMarker FindTheNearestCulturalObject(
                float aX,
                float aY) {

            if (mMarkers != null && mMarkers.size() > 0) {
                return mMarkers.get(0);
            }

            return null;
        }

        /**
         * This method check if the parameters X and Y are in the view or not
         *
         * @param aX
         * @param aY
         * @param aView
         * @return
         */
        private boolean isPointInsideView(float aX, float aY, View aView) {

            int lLocation[] = new int[2];

            aView.getLocationOnScreen(lLocation);

            int lViewX = lLocation[0];
            int lViewY = lLocation[1];

            //TODO
            return true;
            //return ((aX > lViewX && aX <(lViewX + aView.getWidth())) &&
                    //(aY > lViewY && aY < (lViewY + aView.getHeight())));
        }

        /**
         * Retrieve the coordinates touched
         *
         * @param aView
         * @return
         */
        private int[] getCoordinatedTouched(View aView) {

            int lLocation[] = new int[2];

            aView.getLocationOnScreen(lLocation);

            return lLocation;
        }

    //endregion
}
