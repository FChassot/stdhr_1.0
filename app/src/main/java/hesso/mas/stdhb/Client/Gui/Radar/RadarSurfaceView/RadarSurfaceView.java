package hesso.mas.stdhb.Client.Gui.Radar.RadarSurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Tools.DoubleUtil;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;

/**
 * Created by chf on 23.12.2016.
 */
public class RadarSurfaceView extends SurfaceView implements Runnable {

    Thread thread = null;

    SurfaceHolder surfaceHolder;

    volatile boolean running = false;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Random random;

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

    /**
     *
     * @param context
     */
    public RadarSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public RadarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        //do stuff that was in your original constructor...
        surfaceHolder = getHolder();

        random = new Random();
    }

    /**
     *
     */
    public void onResumeMySurfaceView() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     *
     */
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
                Canvas canvas = surfaceHolder.lockCanvas();

                int canvasWidth = this.getWidth();
                int canvasHeight = this.getHeight();

                // Calculate the maximum diameter of the radar possible according
                // to the dimensions of the view
                int maxDiameterOfTheRadarView = Math.min(canvasWidth, canvasHeight);

                Paint radarPaint = mLatestPaint[0];

                int posX = (maxDiameterOfTheRadarView / 2);
                int posY = (maxDiameterOfTheRadarView / 2);
                int radiusOfCircle = (posX - 1);

                // Draw the radar on the view
                drawRadar(
                        canvas,
                        radarPaint,
                        posX,
                        posY,
                        radiusOfCircle);

                // Draw the marker on the view
                drawMarkers(
                        canvas,
                        maxDiameterOfTheRadarView);

                mAlpha -= 3;

                if (mAlpha < -360) mAlpha = 0;

                double angle = Math.toRadians(mAlpha);

                int lOffsetX =  (int) (posX + (float)(posX * Math.cos(angle)));
                int lOffsetY = (int) (posY - (float)(posY * Math.sin(angle)));

                mLatestPoint[0]= new Point(lOffsetX, lOffsetY);

                for (int index = POINT_ARRAY_SIZE-1; index > 0; index--) {
                    mLatestPoint[index] = mLatestPoint[index-1];
                }

                for (int index = 0; index < POINT_ARRAY_SIZE; index++) {
                    Point point = mLatestPoint[index];

                    if (point != null) {
                        canvas.drawLine(
                                posX,
                                posY,
                                point.x,
                                point.y,
                                mLatestPaint[index]);
                    }
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
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
     * @param canvas Canvas hosts the draw calls
     * @param radarMarker The marker to draw
     * @param maxRadiusOfRadar
     */
    private void drawMarker(
            Canvas canvas,
            RadarMarker radarMarker,
            int maxRadiusOfRadar) {

        Checks.AssertNotNull(canvas, "canvas");
        Checks.AssertNotNull(radarMarker, "radarMarker");
        Checks.AssertIsStrictPositive(maxRadiusOfRadar, "maxRadiusOfRadar");

        // Paint object allows to describe the colors and styles for marker
        Paint markerPaint = new Paint();

        markerPaint.setColor(Color.WHITE);
        markerPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(
                radarMarker.getPositionX(),
                radarMarker.getPositionY(),
                (((maxRadiusOfRadar / 2) - 1) >> 5),
                markerPaint);
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

    /**
     * This method allows to get the markers contained in the list held
     * by the RadarView class
     *
     * @return
     */
    public synchronized List<RadarMarker> getMarkers() {
        return mMarkers;
    }

}
