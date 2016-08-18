package hesso.mas.stdhb.Gui.Radar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import hesso.mas.stdhb.Base.Constants.*;

import com.google.android.gms.maps.model.LatLng;

import hesso.mas.stdhb.Gui.GoogleMap.MapsActivity;

/**
 * Created by frede on 15.07.2016.
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

        // défini les markers
        Paint localMarker = new Paint();

        private int fps = 100;
        private boolean showCircles = true;
        private boolean showInterestsFound = true;
        private Context myContext;

        float alpha = 0;

        Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
        Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];
        Point Markers[] = new Point[MARKERS_NUMBER];

        public RadarView(Context context) {
            this(context, null);
        }

        public RadarView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            myContext = context;
            Paint localPaint = new Paint();

            localPaint.setColor(Color.DKGRAY);
            localPaint.setAntiAlias(true);
            localPaint.setStyle(Paint.Style.STROKE);
            localPaint.setStrokeWidth(5.0F);
            localPaint.setAlpha(0);

            localMarker.setColor(Color.YELLOW);
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

        public void startAnimation() {
            mHandler.removeCallbacks(mTick);
            mHandler.post(mTick);
        }

        public void updateMarkers() {
            for (int i=0; i < MARKERS_NUMBER; i++){
                Markers[i] = new Point((i+1)*20+100,(i+1)*5+220);
            }
        }

        public void stopAnimation() {
            mHandler.removeCallbacks(mTick);
        }

        public void setFrameRate(int fps) { this.fps = fps; }
        public int getFrameRate() { return this.fps; }

        public void setShowCircles(boolean showCircles) { this.showCircles = showCircles; }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            int r = Math.min(width, height);

            //canvas.drawRect(0, 0, getWidth(), getHeight(), localPaint);

            int i = r / 2;
            int j = i - 1;
            Paint localPaint = latestPaint[0]; // GREEN

            if (showCircles) {
                canvas.drawCircle(i, i, j, localPaint);
                canvas.drawCircle(i, i, j-25, localPaint);
                canvas.drawCircle(i, i, j * 3 / 4, localPaint);
                canvas.drawCircle(i, i, j >> 1, localPaint);
                canvas.drawCircle(i, i, j >> 2, localPaint);
            }

            if (showInterestsFound) {
                for (Point Marker : Markers) {
                    localPaint.setColor(Color.GREEN);
                    localPaint.setStyle(Paint.Style.FILL);
                    int posX = Marker.x;
                    int posY = Marker.y;
                    int rayon = j >> 5;

                    canvas.drawCircle(posX, posY, rayon, localPaint);
                    localPaint.setColor(Color.DKGRAY);
                    localPaint.setStyle(Paint.Style.STROKE);
                    //Point lMarker = new Point();
                    //lMarker.set(Markers[lIndexMarker].x, Markers[lIndexMarker].y);
                    //canvas.drawPoint(lMarker.x, lMarker.y, localPaint);
                    //canvas.Add(lMarker);
                }
            }

            alpha -= 0.5;
            if (alpha < -360) alpha = 0;
            double angle = Math.toRadians(alpha);
            int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
            int offsetY = (int) (i - (float)(i * Math.sin(angle)));

            latestPoint[0]= new Point(offsetX, offsetY);

            for (int x=POINT_ARRAY_SIZE-1; x > 0; x--) {
                latestPoint[x] = latestPoint[x-1];
            }

            int lines = 0;

            for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
                Point point = latestPoint[x];
                if (point != null) {
                    canvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
                }
            }

            //lines = 0;
            for (Point p : latestPoint) if (p != null) lines++;

            boolean debug = false;

            if (debug) {
                StringBuilder sb = new StringBuilder(" >> ");
                for (Point p : latestPoint) {
                    if (p != null) sb.append(" (" + p.x + "x" + p.y + ")");
                }

                Log.d(LOG, sb.toString());
                //  " - R:" + r + ", i=" + i +
                //  " - Size: " + width + "x" + height +
                //  " - Angle: " + angle +
                //  " - Offset: " + offsetX + "," + offsetY);
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float xCord = 0;
            float yCord = 0;

            if(event.getAction()==MotionEvent.ACTION_UP){
                xCord=event.getX();
                yCord = event.getY();
            }

            //Toast.makeText(myContext, "Canvas clicked", Toast.LENGTH_LONG).show();
            Intent lIntent = new Intent(myContext, MapsActivity.class);
            
            LatLng lGpsCoordonates =
                    new LatLng(
                            Double.parseDouble(Float.toString(xCord)),
                            Double.parseDouble(Float.toString(yCord)));

            lIntent.putExtra(BaseConstants.Attr_Gps_Coordonates, lGpsCoordonates);

            myContext.startActivity(lIntent);

            return true;
        }
}
