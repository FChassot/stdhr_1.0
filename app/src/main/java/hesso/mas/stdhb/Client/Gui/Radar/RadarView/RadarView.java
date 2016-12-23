package hesso.mas.stdhb.Client.Gui.Radar.RadarView;

import android.os.*;

import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;

import hesso.mas.stdhb.Base.Tools.DoubleUtil;
import hesso.mas.stdhb.Client.Gui.Search.SearchActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents the basic building block for user interface components.
 * A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
 * View is the base class for widgets, which are used to create interactive UI components (buttons,
 * text fields, etc.).
 * The ViewGroup subclass is the base class for layouts,
 * which are invisible containers that hold other Views (or other ViewGroups) and define their layout
 * properties.
 *
 */
public class RadarView extends android.view.View {

        // Member variables
        private Handler mHandler = new android.os.Handler();

        private Context mContext;

        private List<RadarMarker> mMarkers = new ArrayList<>();

        private int mAzimuth;

        private final double mTouchScreenSensibility = 35;

        private final int POINT_ARRAY_SIZE = 35;

        private int fps = 100;

        private float mAlpha = 0;

        private Point mLatestPoint[] = new Point[POINT_ARRAY_SIZE];
        private Paint mLatestPaint[] = new Paint[POINT_ARRAY_SIZE];
        private Paint mGridPaint;
        private Paint mTextPaint;

        public RadarMarker mSelectedMarker;

        private double mRadius = 500;

    //region Constructors

        // Constructor
        public RadarView(Context context) {

            this(context, null);
        }

        // Constructor
        public RadarView(
            Context context,
            AttributeSet attributeSet) {

            this(context, attributeSet, 0);
        }

        // Constructor
        public RadarView(
            Context context,
            AttributeSet attributeSet,
            int defStyleAttr) {

            super(context, attributeSet, defStyleAttr);

            mContext = context;

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
            mGridPaint.setTextSize(40.0f);
            mGridPaint.setTextAlign(Paint.Align.CENTER);

            mTextPaint = new Paint();
            mTextPaint.setColor(0xFFFF0000);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setStrokeWidth(2.0f);
            mTextPaint.setTextSize(50.0f);
            mTextPaint.setTextAlign(Paint.Align.LEFT);

            int lAlpha_step = 255 / POINT_ARRAY_SIZE;

            for (int i=0; i < mLatestPaint.length; i++) {
                mLatestPaint[i] = new Paint(lRadarPaint);
                mLatestPaint[i].setAlpha(255 - (i* lAlpha_step));
            }
        }

    //endregion

        public void Radius(double radius) {mRadius = radius;}

        public void Azimuth(int azimuth) {mAzimuth = azimuth;}

    //region Concurrency

        Runnable mTick = new Runnable() {
            @Override
            public void run() {
                // Force the view to draw
                invalidate();
                // Causes the Runnable r to be added to the message queue, to be run after
                // the specified amount of time elapses.
                mHandler.postDelayed(this, 1000 / fps);
            }
        };

        /**
         * This method allows to start the animation
         */
        public void startRadar() {
            // Remove any pending posts of Runnable r that are in the message queue
            mHandler.removeCallbacks(mTick);
            // Causes the Runnable r to be added to the message queue. The runnable will be run
            // on the thread to which this handler is attached.
            mHandler.post(mTick);
        }

        /**
         * This method allows to get the markers contained in the list held
         * by the RadarView class
         *
         * @return
         */
        public synchronized List<RadarMarker> getMarkers() {
            return mMarkers;
        }

        /**
         * This method allows to update the markers received
         * by the radar.
         */
        public synchronized void updateMarkers(
            List<RadarMarker> aMarkers) {
            mMarkers = aMarkers;
        }

        /**
         * This method allows to stop the radar's animation
         */
        public void stopRadar() {
            mHandler.removeCallbacks(mTick);
        }

    //endregion

    //region Radar-draw

        /**
         * The most important step in drawing a custom view is to override the onDraw() method.
         * The parameter to onDraw() is a Canvas object that the view can use to draw itself.
         * The Canvas class defines methods for drawing text, lines, bitmaps, and many other graphics
         * primitives. You can use these methods in onDraw() to create your custom user interface (UI).
         *
         * Before you can call any drawing methods, though, it's necessary to create a Paint object.
         * The next section discusses Paint in more detail.
         *
         * @param canvas hosts the draw calls
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int lCanvasWidth = this.getWidth();
            int lCanvasHeight = this.getHeight();

            // Calculate the maximum diameter of the radar possible according
            // to the dimensions of the view
            int lMaxDiameterOfTheRadarView = Math.min(lCanvasWidth, lCanvasHeight);

            Paint lRadarPaint = mLatestPaint[0];

            int lPosX = (lMaxDiameterOfTheRadarView / 2);
            int lPosY = (lMaxDiameterOfTheRadarView / 2);
            int lRadiusOfCircle = (lPosX - 1);

            // Draw the radar on the view
            drawRadar(
                    canvas,
                    lRadarPaint,
                    lPosX,
                    lPosY,
                    lRadiusOfCircle);

            // Draw the marker on the view
            drawMarkers(
                    canvas,
                    lMaxDiameterOfTheRadarView);

            mAlpha -= 3;

            if (mAlpha < -360) mAlpha = 0;

            double lAngle = Math.toRadians(mAlpha);

            int lOffsetX =  (int) (lPosX + (float)(lPosX * Math.cos(lAngle)));
            int lOffsetY = (int) (lPosY - (float)(lPosY * Math.sin(lAngle)));

            mLatestPoint[0]= new Point(lOffsetX, lOffsetY);

            for (int lIndex = POINT_ARRAY_SIZE-1; lIndex > 0; lIndex--) {
                mLatestPoint[lIndex] = mLatestPoint[lIndex-1];
            }

            for (int lIndex = 0; lIndex < POINT_ARRAY_SIZE; lIndex++) {
                Point lPoint = mLatestPoint[lIndex];

                if (lPoint != null) {
                    canvas.drawLine(
                        lPosX,
                        lPosY,
                        lPoint.x,
                        lPoint.y,
                        mLatestPaint[lIndex]);
                }
            }
        }

        /**
         * Draw the radar in the view
         *
         * @param canvas hosts the draw calls
         * @param radarPaint allows to describe the colors and styles for the radar
         * @param aX the X position of the radar's draw
         * @param aY the Y position of the radar's draw
         * @param radiusOfCircle the radius of the circle
         */
        public void drawRadar(
            Canvas canvas,
            Paint radarPaint,
            int aX,
            int aY,
            Integer radiusOfCircle) {

            String lText1 = getText(mRadius, 4);
            String lText2 = getText(mRadius, 2);
            String lText3 = getText(mRadius, 1.3333333);
            String lText4 = getText(mRadius, 1);

            addNordText(canvas, 650, 650);
            canvas.drawCircle(aX, aY, radiusOfCircle, radarPaint);
            addText(canvas, lText1, aX, ((aY/4)*3)-2, mGridPaint);
            canvas.drawCircle(aX, aY, radiusOfCircle-25, radarPaint);
            addText(canvas, lText2, aX, (aY/2)-2, mGridPaint);
            canvas.drawCircle(aX, aY, radiusOfCircle * 3 / 4, radarPaint);
            addText(canvas, lText3, aX, (aY/4)-2, mGridPaint);
            canvas.drawCircle(aX, aY, radiusOfCircle >> 1, radarPaint);
            canvas.drawCircle(aX, aY, radiusOfCircle >> 2, radarPaint);
            addText(canvas, lText4, aX, 25, mGridPaint);
        }

        /**
         * Add the text Nord in the view
         *
         * @param canvas Canvas hosts the draw calls
         * @param aX the X position of the text
         * @param aY the Y position of the text
         */
        private void addNordText(
            Canvas canvas,
            int aX,
            int aY) {

            Checks.AssertNotNull(canvas, "canvas");
            Checks.AssertIsStrictPositive(aX, "aX");
            Checks.AssertIsStrictPositive(aY, "aY");

            Paint lPaint = new Paint();

            lPaint.setColor(0x0000FFFF);
            lPaint.setAntiAlias(true);
            lPaint.setStyle(Paint.Style.STROKE);
            lPaint.setStrokeWidth(1.0f);
            lPaint.setTextSize(120.0f);
            lPaint.setTextAlign(Paint.Align.CENTER);

            addText(canvas, "NORD", aX, aY, lPaint);
        }

        /**
         * Defines the text which indicate the distance in the radar view
         *
         * @param radius the radius of search in meter
         * @param quotient
         *
         * @return
         */
            private String getText(
                double radius,
                double quotient) {

                Checks.AssertIsStrictPositive(radius, "radius");
                Checks.AssertIsStrictPositive(quotient, "quotient");

                double lRadius = 0.0;

                if (radius >= 1000) {
                    lRadius = (radius / 1000);
                }

                lRadius = (lRadius / quotient);

                lRadius = DoubleUtil.round(lRadius, 2);

                if (radius < 1000) {
                    return lRadius + "m";
                } else {
                    return lRadius + "km";
                }
            }

        /**
         * Draws the markers on the view.
         *
         * @param canvas Canvas hosts the draw calls
         * @param maxRadiusOfRadar
         */
        public void drawMarkers(
            Canvas canvas,
            int maxRadiusOfRadar) {

            Checks.AssertNotNull(canvas, "canvas");
            Checks.AssertIsStrictPositive(maxRadiusOfRadar, "maxRadiusOfRadar");

            // Paint object allows to describe the colors and styles for marker
            Paint markerPaint = new Paint();

            markerPaint.setColor(Color.WHITE);
            markerPaint.setStrokeWidth(10);
            markerPaint.setStyle(Paint.Style.FILL);

            List<RadarMarker> lMarkers = getMarkers();

            if (lMarkers != null){
                RadarMarker lColorMarker = null;

                for (RadarMarker lMarker : lMarkers) {
                    if (this.mSelectedMarker != null) {
                        if (lMarker.getObjectId().equals(this.mSelectedMarker.getObjectId())) {
                            lColorMarker = lMarker;
                        }
                    }
                }

                for (RadarMarker lMarker : lMarkers) {
                    if (lColorMarker != null) {
                        if (lMarker.equals(lColorMarker)) {
                            markerPaint.setColor(Color.RED);
                            addText(canvas, lColorMarker.getTitle(), lColorMarker.getPositionX()+10, lColorMarker.getPositionY(), mTextPaint);
                        } else {
                            markerPaint.setColor(Color.WHITE);
                        }
                    }

                    canvas.drawCircle(
                        lMarker.getPositionX(),
                        lMarker.getPositionY(),
                        (((maxRadiusOfRadar / 2) - 1) >> 5),
                        markerPaint);
                    }
            }
        }

        /**
         * Draws a marker on the view.
         *
         * @param canvas Canvas hosts the draw calls
         * @param radarMarker The marker to draw
         * @param maxRadiusOfRadar
         */
        private void drawMarker(
            Canvas  canvas,
            RadarMarker radarMarker,
            int maxRadiusOfRadar) {

            Checks.AssertNotNull(canvas, "canvas");
            Checks.AssertNotNull(radarMarker, "radarMarker");
            Checks.AssertIsStrictPositive(maxRadiusOfRadar, "maxRadiusOfRadar");

            // Paint object allows to describe the colors and styles for marker
            Paint lMarkerPaint = new Paint();

            lMarkerPaint.setColor(Color.WHITE);
            lMarkerPaint.setStyle(Paint.Style.FILL);

            canvas.drawCircle(
                    radarMarker.getPositionX(),
                    radarMarker.getPositionY(),
                    (((maxRadiusOfRadar / 2) - 1) >> 5),
                    lMarkerPaint);
        }

        /**
         * This method allows to add a label in the view. Used for example in
         * our view for indicating the radius of the circle
         *
         * @param canvas Canvas hosts the draw calls
         * @param text The text of the label to draw in the view
         * @param aX The position X of the label's rectangle
         * @param aY The position Y of the label's rectangle
         * @param textPaint allows to describe the colors and styles for the text
         */
        private void addText(
            Canvas canvas,
            String text,
            double aX,
            double aY,
            Paint textPaint) {

            int lX = (int)aX;
            int lY = (int)aY;

            Rect lTextBounds = new Rect();

            mGridPaint.getTextBounds(text, 0, text.length(), lTextBounds);
            lTextBounds.offset(lX - (lTextBounds.width() >> 1), lY);
            lTextBounds.inset(-2, -2);

            canvas.drawText(text, lX, lY, textPaint);
        }

    //endregion

    //region Help-methods

        /**
         * Called when a touch screen motion event occurs.
         *
         * @param motionEvent
         *
         * @return True when a cultural object has been detected
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            float lOnTouchXCoordinate;
            float lOnTouchYCoordinate;

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                lOnTouchXCoordinate = motionEvent.getX();
                lOnTouchYCoordinate = motionEvent.getY();
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
                mSelectedMarker = lCulturalObject;

                    // Calculate the distance on the view between the point touched and the cultural
                    // object
                    double lDistance =
                        RadarHelper.calculateDistanceInTheViewBetweenTwoPoints(
                        lOnTouchXCoordinate,
                        lOnTouchYCoordinate,
                        lCulturalObject.getPositionX(),
                        lCulturalObject.getPositionY());

                    // When the point touched by the user near enough from the cultural object then
                    // this one will be selected
                    if (((-mTouchScreenSensibility) < lDistance) && (lDistance > mTouchScreenSensibility)) {
                        return false;
                    }

                    if (true) {
                        Intent lIntent = new Intent(mContext, MapsActivity.class);

                        Bundle lBundle = new Bundle();

                        RadarMarker lCurrentUserMarker = new RadarMarker();

                        Location lCurrentUserLocation = getCurrentLocation();

                        if (lCurrentUserLocation != null) {
                            lCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
                            lCurrentUserMarker.setLatitude(lCurrentUserLocation.getLatitude());
                            lCurrentUserMarker.setLongitude(lCurrentUserLocation.getLongitude());
                        }
                        else {
                            lCurrentUserMarker.setLatitude(46.2333);
                            lCurrentUserMarker.setLongitude(7.35);
                            lCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
                        }

                        lBundle.putParcelable(MapsActivity.USER_MARKER, lCurrentUserMarker);
                        lBundle.putParcelable(MapsActivity.RADAR_MARKER, lCulturalObject);

                        if (mMarkers != null && mMarkers.size() > 0) {
                            lBundle.putParcelableArrayList(
                                    MapsActivity.RADAR_MARKER_ARRAY,
                                    (ArrayList<? extends Parcelable>) mMarkers);
                        }

                        lIntent.putExtras(lBundle);

                        mContext.startActivity(lIntent);
                    }
                    else {
                        this.stopRadar();

                        Intent lIntent = new Intent(mContext, SearchActivity.class);

                        Bundle lBundle = new Bundle();

                        RadarMarker lCurrentUserMarker = new RadarMarker();

                        Location lCurrentUserLocation = getCurrentLocation();

                        if (lCurrentUserLocation != null) {
                            lCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
                            lCurrentUserMarker.setLatitude(lCurrentUserLocation.getLatitude());
                            lCurrentUserMarker.setLongitude(lCurrentUserLocation.getLongitude());
                        }
                        else {
                            lCurrentUserMarker.setLatitude(46.2333);
                            lCurrentUserMarker.setLongitude(7.35);
                            lCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
                        }

                        lBundle.putParcelable(MapsActivity.USER_MARKER, lCurrentUserMarker);

                        if (lCulturalObject != null) {
                            lBundle.putParcelable(MapsActivity.RADAR_MARKER, lCulturalObject);
                        }

                        lIntent.putExtras(lBundle);

                        mContext.startActivity(lIntent);
                    }

                return true;
            }

            return false;
        }

        /**
         * This method searches the nearest Cultural object according
         * the point touched on the screen by the user
         *
         * @param aOnTouchXCoordinate
         * @param aOnTouchYCoordinate
         *
         * @return
         */
        private RadarMarker findTheNearestCulturalObject(
            float aOnTouchXCoordinate,
            float aOnTouchYCoordinate) {

            double lDistance = 0.0;

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
         * This method checks if the positions X and Y are located on the view
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

    /**
     * Function to get the user's current location
     *
     * @return the user's current location
     */
    public Location getCurrentLocation() {

        // Declaration of a Location Manager
        LocationManager lLocationManager;

        // Instantiate a GpsLocationListener
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

            // Get GPS services status
            NetworkConnectivity lConnectivity =
                 new NetworkConnectivity(this.getContext());

            boolean lIsGpsEnabled = lConnectivity.isGpsEnabled();

            // Get network status
            boolean lIsNetworkEnabled = lConnectivity.isNetworkAvailable();

            if (lIsGpsEnabled || lIsNetworkEnabled) {
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

                // If GPS Enabled get Location using GPS Services
                if (lIsGpsEnabled) {
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

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return lCurrentLocation;
    }

    //endregion
}
