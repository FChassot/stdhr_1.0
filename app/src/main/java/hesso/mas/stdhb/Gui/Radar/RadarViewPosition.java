package hesso.mas.stdhb.Gui.Radar;

/**
 * Created by chf on 27.09.2016.
 */
public class RadarViewPosition {

    private Integer mX;
    private Integer mY;

    // Constructor
    public RadarViewPosition(Integer aX, Integer aY) {
        mX = aX;
        mY = aY;
    }

    // Setter
    public void setX(Integer aX) {mX = aX;}

    // Getter
    public Integer getX(){ return mX;}

    // Setter
    public void setY(Integer aY) {mY = aY;}

    // Getter
    public Integer getY(){ return mY;}
}
