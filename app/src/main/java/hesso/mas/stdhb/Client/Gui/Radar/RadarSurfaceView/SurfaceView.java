package hesso.mas.stdhb.Client.Gui.Radar.RadarSurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Tools.DoubleUtil;
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
 */
class RadarSurfaceView extends SurfaceView implements Runnable {

    Thread thread = null;
    SurfaceHolder surfaceHolder;

    volatile boolean running = false;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random;

    //
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
    //

    public RadarSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        surfaceHolder = getHolder();
        random = new Random();
    }

    public void onResumeMySurfaceView() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseMySurfaceView() {
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running) {
            if (surfaceHolder.getSurface().isValid()) {
                /*Canvas canvas = surfaceHolder.lockCanvas();
                //... actual drawing on canvas

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);

                int w = canvas.getWidth();
                int h = canvas.getHeight();
                int x = random.nextInt(w - 1);
                int y = random.nextInt(h - 1);
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                paint.setColor(0xff000000 + (r << 16) + (g << 8) + b);
                canvas.drawPoint(x, y, paint);

                surfaceHolder.unlockCanvasAndPost(canvas);*/

                int lCanvasWidth = this.getWidth();
                int lCanvasHeight = this.getHeight();
                Canvas canvas = surfaceHolder.lockCanvas();

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
        }
    }

    /**
     * Draw the radar in the view
     *
     * @param aCanvas hosts the draw calls
     * @param aRadarPaint allows to describe the colors and styles for the radar
     * @param aX the X position of the radar's draw
     * @param aY the Y position of the radar's draw
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
        String lText3 = getText(mRadius, 1.3333333);
        String lText4 = getText(mRadius, 1);

        addNordText(aCanvas, 650, 650);
        aCanvas.drawCircle(aX, aY, aRadiusOfCircle, aRadarPaint);
        addText(aCanvas, lText1, aX, ((aY/4)*3)-2, mGridPaint);
        aCanvas.drawCircle(aX, aY, aRadiusOfCircle-25, aRadarPaint);
        addText(aCanvas, lText2, aX, (aY/2)-2, mGridPaint);
        aCanvas.drawCircle(aX, aY, aRadiusOfCircle * 3 / 4, aRadarPaint);
        addText(aCanvas, lText3, aX, (aY/4)-2, mGridPaint);
        aCanvas.drawCircle(aX, aY, aRadiusOfCircle >> 1, aRadarPaint);
        aCanvas.drawCircle(aX, aY, aRadiusOfCircle >> 2, aRadarPaint);
        addText(aCanvas, lText4, aX, 25, mGridPaint);
    }

    /**
     * Add the text Nord in the view
     *
     * @param aCanvas Canvas hosts the draw calls
     * @param aX the X position of the text
     * @param aY the Y position of the text
     */
    private void addNordText(
            Canvas aCanvas,
            int aX,
            int aY) {

        Checks.AssertNotNull(aCanvas, "aCanvas");
        Checks.AssertIsStrictPositive(aX, "aX");
        Checks.AssertIsStrictPositive(aY, "aY");

        Paint lPaint = new Paint();

        lPaint.setColor(0x0000FFFF);
        lPaint.setAntiAlias(true);
        lPaint.setStyle(Paint.Style.STROKE);
        lPaint.setStrokeWidth(1.0f);
        lPaint.setTextSize(120.0f);
        lPaint.setTextAlign(Paint.Align.CENTER);

        addText(aCanvas, "NORD", aX, aY, lPaint);
    }

    /**
     * Defines the text which indicate the distance in the radar view
     *
     * @param aRadius the radius of search in meter
     * @param aQuotient
     *
     * @return
     */
    private String getText(
            double aRadius,
            double aQuotient) {

        Checks.AssertIsStrictPositive(aRadius, "aRadius");
        Checks.AssertIsStrictPositive(aQuotient, "aQuotient");

        double lRadius = 0.0;

        if (aRadius >= 1000) {
            lRadius = (aRadius / 1000);
        }

        lRadius = (lRadius / aQuotient);

        lRadius = DoubleUtil.round(lRadius, 2);

        if (aRadius < 1000) {
            return lRadius + "m";
        } else {
            return lRadius + "km";
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

        Checks.AssertNotNull(aCanvas, "aCanvas");
        Checks.AssertIsStrictPositive(aMaxRadiusOfRadar, "aMaxRadiusOfRadar");

        // Paint object allows to describe the colors and styles for marker
        Paint lMarkerPaint = new Paint();

        lMarkerPaint.setColor(Color.WHITE);
        lMarkerPaint.setStrokeWidth(10);
        lMarkerPaint.setStyle(Paint.Style.FILL);

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
                        lMarkerPaint.setColor(Color.RED);
                        addText(aCanvas, lColorMarker.getTitle(), lColorMarker.getPositionX()+10, lColorMarker.getPositionY(), mTextPaint);
                    } else {
                        lMarkerPaint.setColor(Color.WHITE);
                    }
                }

                aCanvas.drawCircle(
                        lMarker.getPositionX(),
                        lMarker.getPositionY(),
                        (((aMaxRadiusOfRadar / 2) - 1) >> 5),
                        lMarkerPaint);
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

        Checks.AssertNotNull(aCanvas, "aCanvas");
        Checks.AssertNotNull(aRadarMarker, "aRadarMarker");
        Checks.AssertIsStrictPositive(aMaxRadiusOfRadar, "aMaxRadiusOfRadar");

        // Paint object allows to describe the colors and styles for marker
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
     * @param aText The text of the label to draw in the view
     * @param aX The position X of the label's rectangle
     * @param aY The position Y of the label's rectangle
     * @param aTextPaint allows to describe the colors and styles for the text
     */
    private void addText(
            Canvas aCanvas,
            String aText,
            double aX,
            double aY,
            Paint aTextPaint) {

        int lX = (int)aX;
        int lY = (int)aY;

        Rect lTextBounds = new Rect();

        mGridPaint.getTextBounds(aText, 0, aText.length(), lTextBounds);
        lTextBounds.offset(lX - (lTextBounds.width() >> 1), lY);
        lTextBounds.inset(-2, -2);

        aCanvas.drawText(aText, lX, lY, aTextPaint);
    }

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
}