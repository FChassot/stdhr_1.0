package hesso.mas.stdhb.Client.Gui.Radar.RenderView;

import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Client.Gui.Citizen.SearchActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents the basic building block for user interface components.
 * A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
 * View is the base class for widgets, which are used to create interactive UI components (buttons, text fields, etc.).
 * The ViewGroup subclass is the base class for layouts,
 * which are invisible containers that hold other Views (or other ViewGroups) and define their layout properties.
 *
 */
public class RadarView extends android.view.View {

        Handler mHandler = new android.os.Handler();

        private Context mContext;

        private List<RadarMarker> mMarkers;

        public Double mRadius = 500.0;

        private final int POINT_ARRAY_SIZE = 35;

        private int fps = 100;

        float lAlpha = 0;

        Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
        Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];

        private android.graphics.Paint mGridPaint;

        /**
         * Utility rect for calculating the ring labels
         */
        private Rect mTextBounds = new Rect();

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

        mContext = aContext;

        Paint lRadarPaint = new Paint();

        lRadarPaint.setColor(Color.RED);
        lRadarPaint.setAntiAlias(true);
        lRadarPaint.setStyle(Paint.Style.STROKE);
        lRadarPaint.setStrokeWidth(5.0F);
        lRadarPaint.setAlpha(0);

        // Paint used for the rings and ring text
        mGridPaint = new Paint();
        mGridPaint.setColor(0xFFFFFFFF);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setStrokeWidth(1.0f);
        mGridPaint.setTextSize(30.0f);
        mGridPaint.setTextAlign(Paint.Align.CENTER);

        int lAlpha_step = 255 / POINT_ARRAY_SIZE;

        for (int i=0; i < latestPaint.length; i++) {
            latestPaint[i] = new Paint(lRadarPaint);
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
        public void startRadar() {
            mHandler.removeCallbacks(mTick);
            mHandler.post(mTick);
        }

        /**
         * This method allows to stop the radar's animation
         */
        public void stopRadar() {
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
         * @param aCanvas hosts the draw calls
         */
        @Override
        protected void onDraw(Canvas aCanvas) {
            super.onDraw(aCanvas);

            Paint lRadarPaint = latestPaint[0];

            int lCanvasWidth = this.getWidth();
            int lCanvasHeight = this.getHeight();

            // Calculates the maximum diameter of the radar possible according to the dimensions of the view
            int lMaxDiameterOfTheRadarView = Math.min(lCanvasWidth, lCanvasHeight);

            int lPosX = lMaxDiameterOfTheRadarView / 2;
            int lPosY = lMaxDiameterOfTheRadarView / 2;
            int lRadiusOfCircle = lPosX - 1;

            drawRadar(aCanvas, lRadarPaint, lPosX, lPosY, lRadiusOfCircle);
            drawMarkers(aCanvas, lMaxDiameterOfTheRadarView);

            lAlpha -= 3;

            if (lAlpha < -360) lAlpha = 0;

            double lAngle = Math.toRadians(lAlpha);

            int lOffsetX =  (int) (lPosX + (float)(lPosX * Math.cos(lAngle)));
            int lOffsetY = (int) (lPosY - (float)(lPosY * Math.sin(lAngle)));

            latestPoint[0]= new Point(lOffsetX, lOffsetY);

            for (int x = POINT_ARRAY_SIZE-1; x > 0; x--) {
                latestPoint[x] = latestPoint[x-1];
            }

            for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
                Point point = latestPoint[x];

                if (point != null) {
                    aCanvas.drawLine(lPosX, lPosY, point.x, point.y, latestPaint[x]);
                }
            }
        }

    //region Design-radar

        /**
         * Draw the radar in the view
         *
         * @param aCanvas hosts the draw calls
         * @param aRadarPaint allows to describe the colors and styles for the radar
         * @param aX the X coordinate of the center of the circle
         * @param aY the Y coordinate of the center of the circle
         * @param aRadiusOfCircle the radius of the circle
         */
        public void drawRadar(
            Canvas aCanvas,
            Paint aRadarPaint,
            int aX,
            int aY,
            Integer aRadiusOfCircle) {

            String lText1 = getText(mRadius, 4);
            String lText2 = getText(mRadius, 2);
            String lText3 = getText(mRadius, (3/4));
            String lText4 = getText(mRadius, 1);

            aCanvas.drawCircle(aX, aY, aRadiusOfCircle, aRadarPaint);
            addText(aCanvas, lText1, aX, ((aY/4)*3)-2);
            aCanvas.drawCircle(aX, aY, aRadiusOfCircle-25, aRadarPaint);
            addText(aCanvas, lText2, aX, (aY/2)-2);
            aCanvas.drawCircle(aX, aY, aRadiusOfCircle * 3 / 4, aRadarPaint);
            addText(aCanvas, lText3, aX, (aY/4)-2);
            aCanvas.drawCircle(aX, aY, aRadiusOfCircle >> 1, aRadarPaint);
            aCanvas.drawCircle(aX, aY, aRadiusOfCircle >> 2, aRadarPaint);
            addText(aCanvas, lText4, aX, 22);

        }

    /**
     * Give the text
     *
     * @param aRadius
     * @param aQuotient
     *
     * @return
     */
        private String getText(
            double aRadius,
            float aQuotient) {

            Double lRadius = 0.0;

            if (aRadius >= 1000) {
                lRadius = (aRadius / 1000);
            }

            lRadius = (lRadius/aQuotient);

            if (aRadius < 1000) {
                return lRadius.toString() + "m";
            } else {
                return lRadius.toString() + "km";
            }
        }

        /**
         * Draws the markers on the view.
         *
         * @param aCanvas Canvas hosts the draw calls
         * @param aMaxRadiusOfRadar
         */
        public void drawMarkers(
            Canvas aCanvas,
            int aMaxRadiusOfRadar) {

            Paint lMarkerPaint = new Paint();

            lMarkerPaint.setColor(Color.WHITE);
            lMarkerPaint.setStrokeWidth(10);
            lMarkerPaint.setStyle(Paint.Style.FILL);

            List<RadarMarker> lMarkers = getMarkers();

            if (lMarkers != null){
                if (lMarkers.size() > 0) {
                    for (RadarMarker lMarker : lMarkers) {
                        aCanvas.drawCircle(
                            lMarker.getPositionX(),
                            lMarker.getPositionY(),
                            (((aMaxRadiusOfRadar / 2) - 1) >> 5),
                            lMarkerPaint);
                    }
                }
            }
        }

        /**
         * Draws a marker on the view.
         *
         * @param aCanvas Canvas hosts the draw calls
         * @param aRadarMarker The marker to draw
         * @param aMaxRadiusOfRadar
         */
        private void drawMarker(
            Canvas aCanvas,
            RadarMarker aRadarMarker,
            int aMaxRadiusOfRadar) {

            Paint lMarkerPaint = new Paint();

            lMarkerPaint.setColor(Color.WHITE);
            lMarkerPaint.setStyle(Paint.Style.FILL);

            aCanvas.drawCircle(
                    aRadarMarker.getPositionX(),
                    aRadarMarker.getPositionY(),
                    (((aMaxRadiusOfRadar / 2) - 1) >> 5),
                    lMarkerPaint);
        }

        /**
         * This method allows to add a label in the view. Used for example in
         * our view for indicating the radius of the circle
         *
         * @param aCanvas Canvas hosts the draw calls
         * @param aDisplayText The text of the label to draw in the view
         * @param x The position in x of the label's rectangle
         * @param y The position in y of the label's rectangle
         */
        private void addText(
            Canvas aCanvas,
            String aDisplayText,
            int x,
            int y) {

            mGridPaint.getTextBounds(aDisplayText, 0, aDisplayText.length(), mTextBounds);
            mTextBounds.offset(x - (mTextBounds.width() >> 1), y);
            mTextBounds.inset(-2, -2);
            aCanvas.drawText(aDisplayText, x, y, mGridPaint);
        }
    //endregion

    //region Help-methods

        /**
         * Called when a touch screen motion event occurs.
         *
         * @param aMotionEvent
         *
         * @return True when a cultural object has been detected
         */
        @Override
        public boolean onTouchEvent(MotionEvent aMotionEvent) {

            float lOnTouchXCoordinate;
            float lOnTouchYCoordinate;

            if(aMotionEvent.getAction() == MotionEvent.ACTION_DOWN){
                lOnTouchXCoordinate = aMotionEvent.getX();
                lOnTouchYCoordinate = aMotionEvent.getY();
            }
            else {
                return false;
            }

            // Check if the click in the view has been done
            //if (!isPointInsideView(lOnTouchXCoordinate, lOnTouchYCoordinate, this)) {
                //return false;
            //}

            RadarMarker lCulturalObject =
                findTheNearestCulturalObject(
                    lOnTouchXCoordinate,
                    lOnTouchYCoordinate);

              if (lCulturalObject != null) {
                    double lDistance =
                        RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                        lOnTouchXCoordinate,
                        lOnTouchYCoordinate,
                        lCulturalObject.getPositionX(),
                        lCulturalObject.getPositionY());

                    if ((-10.0 < lDistance) && (lDistance > 10.0)) { return false;}

                    if (false) {
                        Intent lIntent = new Intent(mContext, MapsActivity.class);

                        Bundle lBundle = new Bundle();

                        RadarMarker lCurrentUserMarker = new RadarMarker();

                        Location lCurrentLocation = getUserCurrentLocation();

                        if (lCurrentLocation != null) {
                            lCurrentUserMarker.setTitle("Citizen radar's user");
                            lCurrentUserMarker.setLatitude(lCurrentLocation.getLatitude());
                            lCurrentUserMarker.setLongitude(lCurrentLocation.getLongitude());
                        }
                        else{
                            lCurrentUserMarker.setLatitude(46.2333);
                            lCurrentUserMarker.setLongitude(7.35);
                            lCurrentUserMarker.setTitle("Citizen radar's user");
                        }

                        lBundle.putParcelable(MapsActivity.USER_MARKER, lCurrentUserMarker);
                        lBundle.putParcelable(MapsActivity.RADAR_MARKER, lCulturalObject);

                        if (mMarkers != null && mMarkers.size() > 0) {
                            /*lBundle.putParcelable(
                                    MapsActivity.RADAR_MARKER_ARRAY,
                                    (Parcelable) mMarkers);*/
                        }

                        lIntent.putExtras(lBundle);

                        mContext.startActivity(lIntent);
                    }
                      else {
                        Intent lIntent = new Intent(mContext, SearchActivity.class);

                        Bundle lBundle = new Bundle();

                        RadarMarker lCurrentUserMarker = new RadarMarker();

                        lCurrentUserMarker.setLatitude(46.2333);
                        lCurrentUserMarker.setLongitude(7.35);
                        lCurrentUserMarker.setTitle("Citizen radar's user");

                        lBundle.putParcelable(MapsActivity.USER_MARKER, lCurrentUserMarker);
                        lBundle.putParcelable(MapsActivity.RADAR_MARKER, lCulturalObject);

                        lIntent.putExtras(lBundle);

                        mContext.startActivity(lIntent);
                    }

                return true;
            }

            return false;
        }

        /**
         * This method searches the nearest Cultural object
         *
         * @param aOnTouchXCoordinate
         * @param aOnTouchYCoordinate
         *
         * @return
         */
        private RadarMarker findTheNearestCulturalObject(
            float aOnTouchXCoordinate,
            float aOnTouchYCoordinate) {

            double lDistance = 0;

            RadarMarker lNearestMarker = null;

            if (mMarkers != null){
                for (RadarMarker lMarker : mMarkers) {
                    double lHypotenuse =
                        RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                            aOnTouchXCoordinate,
                            aOnTouchYCoordinate,
                            lMarker.getPositionX(),
                            lMarker.getPositionY());

                    if (lDistance == 0) {
                        lDistance = Math.abs(lHypotenuse);
                        lNearestMarker = lMarker;
                    }

                    if (lDistance != 0 && (Math.abs(lHypotenuse) < Math.abs(lDistance))) {
                        lNearestMarker = lMarker;
                        lDistance = Math.abs(lHypotenuse);
                    }
                }
            }

            return lNearestMarker;
        }

        /**
         * This method check if the parameters X and Y are in the view or not
         *
         * @param aX
         * @param aY
         * @param aView
         *
         * @return yes, if the point is inside the view
         */
        private boolean isPointInsideView(
            float aX,
            float aY,
            View aView) {

            int lLocation[] = new int[2];

            aView.getLocationOnScreen(lLocation);
            int lXPositionOnScreen = lLocation[0];
            int lYPositionOnScreen = lLocation[1];

            return ((aX > lXPositionOnScreen && aX <(lXPositionOnScreen + aView.getWidth())) &&
                    (aY > lYPositionOnScreen && aY < (lYPositionOnScreen + aView.getHeight())));
        }

    //endregion

    /**
     * Function to get the user's current location
     *
     * @return the user's current location
     */
    public Location getUserCurrentLocation() {

        // Declaring a Location Manager
        LocationManager lLocationManager;

        // Instanciate a GpsLocationListener
        LocationListener lLocationListener =
            new GpsLocationListener(this.getContext());

        // The minimum distance to change Updates in meters
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
        final long MIN_TIME_BW_UPDATES = 1; // 1 minute

        Location lCurrentLocation = null;

        try {
            lLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

            // get GPS services status
            NetworkConnectivity lConnectivity =
                 new NetworkConnectivity(this.getContext());

            Boolean lGpsEnabled = lConnectivity.IsGpsEnabled();

            // get network status
            Boolean lIsNetworkEnabled = lConnectivity.IsNetworkAvailable();

            if (!lGpsEnabled && !lIsNetworkEnabled) {
                // no network provider is enabled
            }
            else {
                if (lIsNetworkEnabled) {
                    lLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        lLocationListener);

                    if (lLocationManager != null) {
                        lCurrentLocation =
                            lLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                // if GPS Enabled get Location using GPS Services
                if (lGpsEnabled) {
                    lLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        lLocationListener);

                        if (lLocationManager != null) {
                            lCurrentLocation =
                                    lLocationManager.getLastKnownLocation(
                                            LocationManager.GPS_PROVIDER);
                        }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lCurrentLocation;
    }
}
