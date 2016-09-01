package hesso.mas.stdhb.Gui.Radar;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import hesso.mas.stdhb.Base.Constants.*;

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
public class RadarView extends View {

        private final String LOG = "RadarView";
        private final int POINT_ARRAY_SIZE = 45;
        private final int MARKERS_NUMBER = 1;

        // Define the markers
        Paint localMarker = new Paint();

        private int fps = 100;
        //private boolean mDisplayCircles = true;
        private boolean lCitizenInterestsFound = true;
        private Context myContext;

        float lAlpha = 0;

        Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
        Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];
        Point Markers[] = new Point[MARKERS_NUMBER];

        // Constructor
        public RadarView(Context aContext) {
            this(aContext, null);
        }

        // Constructor
        public RadarView(Context aContext, AttributeSet aAttributeSet) {
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

            localPaint.setColor(Color.DKGRAY);
            localPaint.setAntiAlias(true);
            localPaint.setStyle(Paint.Style.STROKE);
            localPaint.setStrokeWidth(5.0F);
            localPaint.setAlpha(0);

            localMarker.setColor(Color.RED);
            localMarker.setAntiAlias(true);
            localMarker.setStyle(Paint.Style.FILL);
            localMarker.setStrokeWidth(5.0F);
            localMarker.setAlpha(0);

            int alpha_step = 255 / POINT_ARRAY_SIZE;

            for (int i=0; i < latestPaint.length; i++) {
                latestPaint[i] = new Paint(localPaint);
                latestPaint[i].setAlpha(255 - (i* alpha_step));
            }
        }

        android.os.Handler mHandler = new android.os.Handler();

        Runnable mTick = new Runnable() {
            @Override
            public void run() {
                invalidate();
                mHandler.postDelayed(this, 1000 / fps);
            }
        };

        /**
        *
        */
        public void updateMarkers() {
            for (int i=0; i < MARKERS_NUMBER; i++){
                Markers[i] = new Point((i+1)*20+100,(i+1)*5+220);
            }
        }

        /**
         *
         */
        public void startAnimation() {
            mHandler.removeCallbacks(mTick);
            mHandler.post(mTick);
        }

        /**
         *
         */
        public void stopAnimation() {
                mHandler.removeCallbacks(mTick);
            }

        public void setFrameRate(int fps) { this.fps = fps; }
        public int getFrameRate() { return this.fps; }

        //public void setShowCircles(boolean aDisplayCircles) { this.mDisplayCircles = aDisplayCircles; }


        /**
         *
         * The most important step in drawing a custom view is to override the onDraw() method. The parameter to onDraw()
         * is a Canvas object that the view can use to draw itself. The Canvas class defines methods for drawing text,
         * lines, bitmaps, and many other graphics primitives. You can use these methods in onDraw() to create your
         * custom user interface (UI).
         *
         * Before you can call any drawing methods, though, it's necessary to create a Paint object. The next section
         * discusses Paint in more detail.
         *
         * @param aCanvas
         */
        @Override
        protected void onDraw(Canvas aCanvas) {
            super.onDraw(aCanvas);

            Paint lLocalPaint = latestPaint[0]; // GREEN

            int lCanvasWidth = this.getWidth();
            int lCanvasHeight = this.getHeight();

            int lMaxRayOfRadar = Math.min(lCanvasWidth, lCanvasHeight);

            int i = lMaxRayOfRadar / 2;
            int j = i - 1;

            //if (showCircles) {
            aCanvas.drawCircle(i, i, j, lLocalPaint);
            aCanvas.drawCircle(i, i, j-25, lLocalPaint);
            aCanvas.drawCircle(i, i, j * 3 / 4, lLocalPaint);
            aCanvas.drawCircle(i, i, j >> 1, lLocalPaint);
            aCanvas.drawCircle(i, i, j >> 2, lLocalPaint);
            //}

            if (lCitizenInterestsFound) {
                for (Point Marker : Markers) {
                    lLocalPaint.setColor(Color.RED);
                    lLocalPaint.setStyle(Paint.Style.FILL);

                    // int rayon = j >> 5;
                    aCanvas.drawCircle(Marker.x, Marker.y, j >> 5, lLocalPaint);

                    lLocalPaint.setColor(Color.DKGRAY);
                    lLocalPaint.setStyle(Paint.Style.STROKE);
                    //Point lMarker = new Point();
                    //lMarker.set(Markers[lIndexMarker].x, Markers[lIndexMarker].y);
                    //canvas.drawPoint(lMarker.x, lMarker.y, localPaint);
                    //canvas.Add(lMarker);
                }
            }

            lAlpha -= 1; //initially -0.5

            if (lAlpha < -360) lAlpha = 0;

            double lAngle = Math.toRadians(lAlpha);

            int lOffsetX =  (int) (i + (float)(i * Math.cos(lAngle)));
            int lOffsetY = (int) (i - (float)(i * Math.sin(lAngle)));

            latestPoint[0]= new Point(lOffsetX, lOffsetY);

            for (int x=POINT_ARRAY_SIZE-1; x > 0; x--) {
                latestPoint[x] = latestPoint[x-1];
            }

            //int lines = 0;

            for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
                Point point = latestPoint[x];

                if (point != null) {
                    aCanvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
                }
            }

            //lines = 0;
            //for (Point p : latestPoint) if (p != null) lines++;

            /*if (false) {
                StringBuilder sb = new StringBuilder(" >> ");
                for (Point p : latestPoint) {
                    if (p != null) sb.append(" (" + p.x + "x" + p.y + ")");
                }

                Log.d(LOG, sb.toString());
                //  " - R:" + r + ", i=" + i +
                //  " - Size: " + width + "x" + height +
                //  " - Angle: " + angle +
                //  " - Offset: " + offsetX + "," + offsetY);
            }*/
        }

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
            int lAction = event.getAction();

            if(lAction == MotionEvent.ACTION_UP){
                lXCoordinate = event.getX();
                lYCoordinate = event.getY();
            }

            // Check if the click in the view has been done
            if (!isPointInsideView(lXCoordinate, lYCoordinate, this)) {
                Toast.makeText(myContext, "Outside the radar", Toast.LENGTH_LONG).show();
            }

            Intent lIntent = new Intent(myContext, MapsActivity.class);
            
            LatLng lGpsCoordonates =
                    new LatLng(
                            Double.parseDouble(Float.toString(lXCoordinate)),
                            Double.parseDouble(Float.toString(lYCoordinate)));

            lIntent.putExtra(BaseConstants.Attr_Gps_Coordinates, lGpsCoordonates);

            myContext.startActivity(lIntent);

            return true;
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

            return ((aX > lViewX && aX <(lViewX + aView.getWidth())) &&
                    (aY > lViewY && aY < (lViewY + aView.getHeight())));
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
}
